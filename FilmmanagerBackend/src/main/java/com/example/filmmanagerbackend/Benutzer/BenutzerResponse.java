package com.example.filmmanagerbackend.Benutzer;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BenutzerResponse {

    private Integer id;
    private String benutzername;
    private String email;
    private LocalDate geburtsdatum;
    private Geschlecht geschlecht;
    private LocalDateTime erstelltAm;
    private boolean aktiv;
    private boolean administrator;

    public static BenutzerResponse fromEntity(Benutzer benutzer) {
        BenutzerResponse response = new BenutzerResponse();
        response.id = benutzer.getId();
        response.benutzername = benutzer.getBenutzername();
        response.email = benutzer.getEmail();
        response.geburtsdatum = benutzer.getGeburtsdatum();
        response.geschlecht = benutzer.getGeschlecht();
        response.erstelltAm = benutzer.getErstelltAm();
        response.aktiv = benutzer.isAktiv();
        response.administrator = "admin".equalsIgnoreCase(benutzer.getBenutzername());
        return response;
    }

    public Integer getId() {
        return id;
    }

    public String getBenutzername() {
        return benutzername;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getGeburtsdatum() {
        return geburtsdatum;
    }

    public Geschlecht getGeschlecht() {
        return geschlecht;
    }

    public LocalDateTime getErstelltAm() {
        return erstelltAm;
    }

    public boolean isAktiv() {
        return aktiv;
    }

    public boolean isAdministrator() {
        return administrator;
    }
}
