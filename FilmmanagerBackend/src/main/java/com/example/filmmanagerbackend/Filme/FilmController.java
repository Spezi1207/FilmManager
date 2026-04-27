package com.example.filmmanagerbackend.Filme;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/films")
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<FilmResponse> getAllFilms(
            @RequestParam(required = false) String suche,
            @RequestParam(required = false) String kategorie,
            @RequestParam(required = false) Integer jahr,
            @RequestParam(required = false) String sortierung
    ) {
        return filmService.getAllFilms(suche, kategorie, jahr, sortierung);
    }

    @GetMapping("/{id}")
    public FilmResponse getFilm(@PathVariable Integer id) {
        return filmService.getFilmById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmResponse createFilm(@Valid @RequestBody FilmRequest request) {
        return filmService.createFilm(request);
    }

    @PutMapping("/{id}")
    public FilmResponse updateFilm(@PathVariable Integer id, @Valid @RequestBody FilmRequest request) {
        return filmService.updateFilm(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFilm(@PathVariable Integer id) {
        filmService.deleteFilm(id);
    }
}
