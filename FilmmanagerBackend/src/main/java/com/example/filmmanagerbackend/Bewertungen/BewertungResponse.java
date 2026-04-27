package com.example.filmmanagerbackend.Bewertungen;

import java.time.LocalDateTime;

public class BewertungResponse {

    private Integer id;
    private Integer benutzerId;
    private String benutzername;
    private Integer filmId;
    private Integer sterne;
    private String kommentar;
    private LocalDateTime bewertetAm;
    private boolean eigeneBewertung;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBenutzerId() {
        return benutzerId;
    }

    public void setBenutzerId(Integer benutzerId) {
        this.benutzerId = benutzerId;
    }

    public String getBenutzername() {
        return benutzername;
    }

    public void setBenutzername(String benutzername) {
        this.benutzername = benutzername;
    }

    public Integer getFilmId() {
        return filmId;
    }

    public void setFilmId(Integer filmId) {
        this.filmId = filmId;
    }

    public Integer getSterne() {
        return sterne;
    }

    public void setSterne(Integer sterne) {
        this.sterne = sterne;
    }

    public String getKommentar() {
        return kommentar;
    }

    public void setKommentar(String kommentar) {
        this.kommentar = kommentar;
    }

    public LocalDateTime getBewertetAm() {
        return bewertetAm;
    }

    public void setBewertetAm(LocalDateTime bewertetAm) {
        this.bewertetAm = bewertetAm;
    }

    public boolean isEigeneBewertung() {
        return eigeneBewertung;
    }

    public void setEigeneBewertung(boolean eigeneBewertung) {
        this.eigeneBewertung = eigeneBewertung;
    }
}
