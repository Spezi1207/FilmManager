package com.example.filmmanagerbackend.Bewertungen;

import com.example.filmmanagerbackend.Benutzer.Benutzer;
import com.example.filmmanagerbackend.Benutzer.BenutzerService;
import com.example.filmmanagerbackend.Common.ApiException;
import com.example.filmmanagerbackend.Filme.Film;
import com.example.filmmanagerbackend.Filme.FilmService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BewertungService {

    private final BewertungRepository bewertungRepository;
    private final FilmService filmService;
    private final BenutzerService benutzerService;

    public BewertungService(
            BewertungRepository bewertungRepository,
            FilmService filmService,
            BenutzerService benutzerService
    ) {
        this.bewertungRepository = bewertungRepository;
        this.filmService = filmService;
        this.benutzerService = benutzerService;
    }

    public List<BewertungResponse> getBewertungenByFilm(Integer filmId, Integer currentUserId) {
        filmService.loadFilm(filmId);

        return bewertungRepository.findByFilm_IdOrderByBewertetAmDesc(filmId).stream()
                .map(bewertung -> toResponse(bewertung, currentUserId))
                .toList();
    }

    public BewertungResponse saveOrUpdate(Integer filmId, BewertungRequest request) {
        Film film = filmService.loadFilm(filmId);
        Benutzer benutzer = benutzerService.loadBenutzer(request.getBenutzerId());

        if (!benutzer.isAktiv()) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Deaktivierte Benutzer duerfen keine Bewertungen abgeben.");
        }

        Bewertung bewertung = bewertungRepository.findByFilm_IdAndBenutzer_Id(filmId, request.getBenutzerId())
                .orElseGet(Bewertung::new);

        bewertung.setFilm(film);
        bewertung.setBenutzer(benutzer);
        bewertung.setSterne(request.getSterne());
        bewertung.setKommentar(leererTextZuNull(request.getKommentar()));

        return toResponse(bewertungRepository.save(bewertung), request.getBenutzerId());
    }

    public void deleteOwnRating(Integer filmId, Integer benutzerId) {
        Bewertung bewertung = bewertungRepository.findByFilm_IdAndBenutzer_Id(filmId, benutzerId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Bewertung wurde nicht gefunden."));
        bewertungRepository.delete(bewertung);
    }

    private BewertungResponse toResponse(Bewertung bewertung, Integer currentUserId) {
        BewertungResponse response = new BewertungResponse();
        response.setId(bewertung.getId());
        response.setBenutzerId(bewertung.getBenutzer().getId());
        response.setBenutzername(bewertung.getBenutzer().getBenutzername());
        response.setFilmId(bewertung.getFilm().getId());
        response.setSterne(bewertung.getSterne());
        response.setKommentar(bewertung.getKommentar());
        response.setBewertetAm(bewertung.getBewertetAm());
        response.setEigeneBewertung(currentUserId != null && currentUserId.equals(bewertung.getBenutzer().getId()));
        return response;
    }

    private String leererTextZuNull(String text) {
        if (text == null) {
            return null;
        }

        String normalized = text.trim();
        return normalized.isEmpty() ? null : normalized;
    }
}
