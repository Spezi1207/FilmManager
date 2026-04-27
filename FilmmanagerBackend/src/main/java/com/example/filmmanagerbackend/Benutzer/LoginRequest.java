package com.example.filmmanagerbackend.Benutzer;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "Benutzername ist erforderlich.")
    private String benutzername;

    @NotBlank(message = "Passwort ist erforderlich.")
    private String passwort;

    public String getBenutzername() {
        return benutzername;
    }

    public void setBenutzername(String benutzername) {
        this.benutzername = benutzername;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }
}
