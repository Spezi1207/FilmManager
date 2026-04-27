package com.example.filmmanagerbackend.Benutzer;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class BenutzerController {

    private final BenutzerService benutzerService;

    public BenutzerController(BenutzerService benutzerService) {
        this.benutzerService = benutzerService;
    }

    @GetMapping
    public List<BenutzerResponse> getUsers(@RequestParam Integer actorId) {
        return benutzerService.getAlleBenutzer(actorId);
    }

    @GetMapping("/{id}")
    public BenutzerResponse getUser(@PathVariable Integer id) {
        return benutzerService.getBenutzer(id);
    }

    @PutMapping("/{id}")
    public BenutzerResponse updateUser(@PathVariable Integer id, @Valid @RequestBody BenutzerUpdateRequest request) {
        return benutzerService.profilAktualisieren(id, request);
    }

    @PatchMapping("/{id}/deactivate")
    public BenutzerResponse deactivateUser(@PathVariable Integer id, @RequestParam Integer actorId) {
        return benutzerService.kontoDeaktivieren(id, actorId);
    }
}
