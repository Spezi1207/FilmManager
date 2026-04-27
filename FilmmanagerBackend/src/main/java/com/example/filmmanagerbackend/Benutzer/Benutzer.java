package com.example.filmmanagerbackend.Benutzer;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "benutzer")
public class Benutzer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "benutzer_id")
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String benutzername;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "passwort_hash", nullable = false, length = 255)
    private String passwortHash;

    @Column(nullable = false)
    private LocalDate geburtsdatum;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 1)
    private Geschlecht geschlecht;

    @Column(name = "erstellt_am", nullable = false)
    private LocalDateTime erstelltAm;

    @Column(nullable = false)
    private boolean aktiv = true;

    @PrePersist
    public void prePersist() {
        if (erstelltAm == null) {
            erstelltAm = LocalDateTime.now();
        }
    }

    public Integer getId() {
        return id;
    }

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

    public String getPasswortHash() {
        return passwortHash;
    }

    public void setPasswortHash(String passwortHash) {
        this.passwortHash = passwortHash;
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

    public LocalDateTime getErstelltAm() {
        return erstelltAm;
    }

    public boolean isAktiv() {
        return aktiv;
    }

    public void setAktiv(boolean aktiv) {
        this.aktiv = aktiv;
    }
}
