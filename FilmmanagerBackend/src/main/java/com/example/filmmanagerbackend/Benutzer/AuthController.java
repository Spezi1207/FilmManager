package com.example.filmmanagerbackend.Benutzer;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final BenutzerService benutzerService;

    public AuthController(BenutzerService benutzerService) {
        this.benutzerService = benutzerService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody BenutzerRegistrationRequest request) {
        return new AuthResponse("Registrierung erfolgreich.", benutzerService.registrieren(request));
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return new AuthResponse("Anmeldung erfolgreich.", benutzerService.anmelden(request));
    }
}
