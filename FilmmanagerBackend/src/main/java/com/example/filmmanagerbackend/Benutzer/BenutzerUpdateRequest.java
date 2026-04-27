package com.example.filmmanagerbackend.Benutzer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BenutzerUpdateRequest {

    @NotBlank(message = "E-Mail ist erforderlich.")
    @Email(message = "Bitte geben Sie eine gueltige E-Mail-Adresse ein.")
    @Size(max = 100, message = "E-Mail darf maximal 100 Zeichen lang sein.")
    private String email;

    @Size(min = 6, message = "Das Passwort muss mindestens 6 Zeichen lang sein.")
    private String neuesPasswort;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNeuesPasswort() {
        return neuesPasswort;
    }

    public void setNeuesPasswort(String neuesPasswort) {
        this.neuesPasswort = neuesPasswort;
    }
}
