package com.example.filmmanagerbackend.Filme;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class FilmRequest {

    @NotBlank(message = "Titel ist erforderlich.")
    @Size(max = 200, message = "Titel darf maximal 200 Zeichen lang sein.")
    private String titel;

    @NotNull(message = "Erscheinungsdatum ist erforderlich.")
    private LocalDate erscheinungsdatum;

    @NotNull(message = "Filmlaenge ist erforderlich.")
    @Min(value = 1, message = "Filmlaenge muss groesser als 0 sein.")
    private Integer filmlaengeMin;

    @NotBlank(message = "Kategorie ist erforderlich.")
    @Size(max = 50, message = "Kategorie darf maximal 50 Zeichen lang sein.")
    private String kategorie;

    @Size(max = 2000, message = "Beschreibung darf maximal 2000 Zeichen lang sein.")
    private String kurzbeschreibung;

    @Size(max = 500000, message = "Titelbild ist zu gross.")
    private String titelbildPfad;

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
}
