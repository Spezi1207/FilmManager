package com.example.filmmanagerbackend.Filme;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FilmResponse {

    private Integer id;
    private String titel;
    private LocalDate erscheinungsdatum;
    private Integer filmlaengeMin;
    private String filmlaengeAnzeige;
    private String kategorie;
    private String kurzbeschreibung;
    private String titelbildPfad;
    private LocalDateTime hinzugefuegtAm;
    private double durchschnittsbewertung;
    private long bewertungenAnzahl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public LocalDate getErscheinungsdatum() {
        return erscheinungsdatum;
    }

    public void setErscheinungsdatum(LocalDate erscheinungsdatum) {
        this.erscheinungsdatum = erscheinungsdatum;
    }

    public Integer getFilmlaengeMin() {
        return filmlaengeMin;
    }

    public void setFilmlaengeMin(Integer filmlaengeMin) {
        this.filmlaengeMin = filmlaengeMin;
    }

    public String getFilmlaengeAnzeige() {
        return filmlaengeAnzeige;
    }

    public void setFilmlaengeAnzeige(String filmlaengeAnzeige) {
        this.filmlaengeAnzeige = filmlaengeAnzeige;
    }

    public String getKategorie() {
        return kategorie;
    }

    public void setKategorie(String kategorie) {
        this.kategorie = kategorie;
    }

    public String getKurzbeschreibung() {
        return kurzbeschreibung;
    }

    public void setKurzbeschreibung(String kurzbeschreibung) {
        this.kurzbeschreibung = kurzbeschreibung;
    }

    public String getTitelbildPfad() {
        return titelbildPfad;
    }

    public void setTitelbildPfad(String titelbildPfad) {
        this.titelbildPfad = titelbildPfad;
    }

    public LocalDateTime getHinzugefuegtAm() {
        return hinzugefuegtAm;
    }

    public void setHinzugefuegtAm(LocalDateTime hinzugefuegtAm) {
        this.hinzugefuegtAm = hinzugefuegtAm;
    }

    public double getDurchschnittsbewertung() {
        return durchschnittsbewertung;
    }

    public void setDurchschnittsbewertung(double durchschnittsbewertung) {
        this.durchschnittsbewertung = durchschnittsbewertung;
    }

    public long getBewertungenAnzahl() {
        return bewertungenAnzahl;
    }

    public void setBewertungenAnzahl(long bewertungenAnzahl) {
        this.bewertungenAnzahl = bewertungenAnzahl;
    }
}
