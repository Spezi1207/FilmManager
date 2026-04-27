package com.example.filmmanagerbackend.Filme;

import com.example.filmmanagerbackend.Bewertungen.Bewertung;
import com.example.filmmanagerbackend.Bewertungen.BewertungRepository;
import com.example.filmmanagerbackend.Common.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmRepository filmRepository;
    private final BewertungRepository bewertungRepository;

    public FilmService(FilmRepository filmRepository, BewertungRepository bewertungRepository) {
        this.filmRepository = filmRepository;
        this.bewertungRepository = bewertungRepository;
    }

    public List<FilmResponse> getAllFilms(String suche, String kategorie, Integer jahr, String sortierung) {
        List<Film> films = filmRepository.findAll();
        Map<Integer, List<Bewertung>> bewertungenByFilm = bewertungRepository.findAll().stream()
                .collect(Collectors.groupingBy(bewertung -> bewertung.getFilm().getId()));

        return films.stream()
                .filter(film -> passtZurSuche(film, suche))
                .filter(film -> passtZurKategorie(film, kategorie))
                .filter(film -> passtZumJahr(film, jahr))
                .map(film -> toResponse(film, bewertungenByFilm.getOrDefault(film.getId(), List.of())))
                .sorted(buildComparator(sortierung))
                .toList();
    }

    public FilmResponse getFilmById(Integer id) {
        Film film = loadFilm(id);
        return toResponse(film, bewertungRepository.findByFilm_IdOrderByBewertetAmDesc(id));
    }

    public FilmResponse createFilm(FilmRequest request) {
        Film film = new Film();
        uebernehmeFilmDaten(film, request);
        return toResponse(filmRepository.save(film), List.of());
    }

    public FilmResponse updateFilm(Integer id, FilmRequest request) {
        Film film = loadFilm(id);
        uebernehmeFilmDaten(film, request);
        return getFilmById(filmRepository.save(film).getId());
    }

    public void deleteFilm(Integer id) {
        Film film = loadFilm(id);
        bewertungRepository.deleteByFilm_Id(id);
        filmRepository.delete(film);
    }

    public Film loadFilm(Integer id) {
        return filmRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Film wurde nicht gefunden."));
    }

    private void uebernehmeFilmDaten(Film film, FilmRequest request) {
        film.setTitel(request.getTitel().trim());
        film.setErscheinungsdatum(request.getErscheinungsdatum());
        film.setFilmlaengeMin(request.getFilmlaengeMin());
        film.setKategorie(request.getKategorie().trim());
        film.setKurzbeschreibung(leererTextZuNull(request.getKurzbeschreibung()));
        film.setTitelbildPfad(leererTextZuNull(request.getTitelbildPfad()));
    }

    private boolean passtZurSuche(Film film, String suche) {
        if (suche == null || suche.isBlank()) {
            return true;
        }

        return film.getTitel()
                .toLowerCase(Locale.ROOT)
                .contains(suche.trim().toLowerCase(Locale.ROOT));
    }

    private boolean passtZurKategorie(Film film, String kategorie) {
        if (kategorie == null || kategorie.isBlank()) {
            return true;
        }

        return film.getKategorie().equalsIgnoreCase(kategorie.trim());
    }

    private boolean passtZumJahr(Film film, Integer jahr) {
        return jahr == null || film.getErscheinungsdatum().getYear() == jahr;
    }

    private Comparator<FilmResponse> buildComparator(String sortierung) {
        if ("bewertung".equalsIgnoreCase(sortierung)) {
            return Comparator.comparingDouble(FilmResponse::getDurchschnittsbewertung)
                    .reversed()
                    .thenComparing(FilmResponse::getTitel, String.CASE_INSENSITIVE_ORDER);
        }

        if ("erscheinungsdatum".equalsIgnoreCase(sortierung)) {
            return Comparator.comparing(FilmResponse::getErscheinungsdatum)
                    .reversed()
                    .thenComparing(FilmResponse::getTitel, String.CASE_INSENSITIVE_ORDER);
        }

        return Comparator.comparing(FilmResponse::getTitel, String.CASE_INSENSITIVE_ORDER);
    }

    private FilmResponse toResponse(Film film, List<Bewertung> bewertungen) {
        FilmResponse response = new FilmResponse();
        response.setId(film.getId());
        response.setTitel(film.getTitel());
        response.setErscheinungsdatum(film.getErscheinungsdatum());
        response.setFilmlaengeMin(film.getFilmlaengeMin());
        response.setFilmlaengeAnzeige(formatFilmlaenge(film.getFilmlaengeMin()));
        response.setKategorie(film.getKategorie());
        response.setKurzbeschreibung(film.getKurzbeschreibung());
        response.setTitelbildPfad(film.getTitelbildPfad());
        response.setHinzugefuegtAm(film.getHinzugefuegtAm());
        response.setBewertungenAnzahl(bewertungen.size());
        response.setDurchschnittsbewertung(berechneDurchschnitt(bewertungen));
        return response;
    }

    private double berechneDurchschnitt(List<Bewertung> bewertungen) {
        if (bewertungen.isEmpty()) {
            return 0.0;
        }

        double average = bewertungen.stream()
                .mapToInt(Bewertung::getSterne)
                .average()
                .orElse(0.0);

        return BigDecimal.valueOf(average)
                .setScale(1, RoundingMode.HALF_UP)
                .doubleValue();
    }

    private String formatFilmlaenge(Integer minuten) {
        int stunden = minuten / 60;
        int restMinuten = minuten % 60;
        return stunden + "h " + restMinuten + "min";
    }

    private String leererTextZuNull(String text) {
        if (text == null) {
            return null;
        }

        String normalized = text.trim();
        return normalized.isEmpty() ? null : normalized;
    }
}
