package com.example.filmmanagerbackend.Benutzer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class BenutzerRegistrationRequest {

    @NotBlank(message = "Benutzername ist erforderlich.")
    @Size(max = 50, message = "Benutzername darf maximal 50 Zeichen lang sein.")
    private String benutzername;

    @NotBlank(message = "E-Mail ist erforderlich.")
    @Email(message = "Bitte geben Sie eine gueltige E-Mail-Adresse ein.")
    @Size(max = 100, message = "E-Mail darf maximal 100 Zeichen lang sein.")
    private String email;

    @NotBlank(message = "Passwort ist erforderlich.")
    @Size(min = 6, message = "Das Passwort muss mindestens 6 Zeichen lang sein.")
    private String passwort;

    @NotNull(message = "Geburtsdatum ist erforderlich.")
    private LocalDate geburtsdatum;

    @NotNull(message = "Geschlecht ist erforderlich.")
    private Geschlecht geschlecht;

    public String getBenutzername() {
        return benutzername;
    }

    public void setBenutzername(String benutzername) {
        this.benutzername = benutzername;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public LocalDate getGeburtsdatum() {
        return geburtsdatum;
    }

    public void setGeburtsdatum(LocalDate geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    public Geschlecht getGeschlecht() {
        return geschlecht;
    }

    public void setGeschlecht(Geschlecht geschlecht) {
        this.geschlecht = geschlecht;
    }
}
