package com.example.filmmanagerbackend.Benutzer;

public class AuthResponse {

    private final String message;
    private final BenutzerResponse benutzer;

    public AuthResponse(String message, BenutzerResponse benutzer) {
        this.message = message;
        this.benutzer = benutzer;
    }

    public String getMessage() {
        return message;
    }

    public BenutzerResponse getBenutzer() {
        return benutzer;
    }
}
