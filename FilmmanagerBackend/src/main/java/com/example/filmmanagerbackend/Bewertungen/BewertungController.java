package com.example.filmmanagerbackend.Bewertungen;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/films/{filmId}/bewertungen")
public class BewertungController {

    private final BewertungService bewertungService;

    public BewertungController(BewertungService bewertungService) {
        this.bewertungService = bewertungService;
    }

    @GetMapping
    public List<BewertungResponse> getBewertungen(
            @PathVariable Integer filmId,
            @RequestParam(required = false) Integer currentUserId
    ) {
        return bewertungService.getBewertungenByFilm(filmId, currentUserId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BewertungResponse saveBewertung(
            @PathVariable Integer filmId,
            @Valid @RequestBody BewertungRequest request
    ) {
        return bewertungService.saveOrUpdate(filmId, request);
    }

    @DeleteMapping("/{benutzerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBewertung(@PathVariable Integer filmId, @PathVariable Integer benutzerId) {
        bewertungService.deleteOwnRating(filmId, benutzerId);
    }
}
