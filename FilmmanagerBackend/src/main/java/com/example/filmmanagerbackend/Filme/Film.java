package com.example.filmmanagerbackend.Filme;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "film")
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "film_id")
    private Integer id;

    @Column(nullable = false, length = 200)
    private String titel;

    @Column(nullable = false)
    private LocalDate erscheinungsdatum;

    @Column(name = "filmlaenge_min", nullable = false)
    private Integer filmlaengeMin;

    @Column(nullable = false, length = 50)
    private String kategorie;

    @Column(columnDefinition = "TEXT")
    private String kurzbeschreibung;

    @Lob
    @Column(name = "titelbild_pfad")
    private String titelbildPfad;

    @Column(name = "hinzugefuegt_am", nullable = false)
    private LocalDateTime hinzugefuegtAm;

    @PrePersist
    public void prePersist() {
        if (hinzugefuegtAm == null) {
            hinzugefuegtAm = LocalDateTime.now();
        }
    }

    public Integer getId() {
        return id;
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
}
