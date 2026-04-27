package com.example.filmmanagerbackend.Bewertungen;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class BewertungRequest {

    @NotNull(message = "Benutzer-ID ist erforderlich.")
    private Integer benutzerId;

    @NotNull(message = "Sternebewertung ist erforderlich.")
    @Min(value = 1, message = "Es sind nur 1 bis 5 Sterne erlaubt.")
    @Max(value = 5, message = "Es sind nur 1 bis 5 Sterne erlaubt.")
    private Integer sterne;

    @Size(max = 2000, message = "Kommentar darf maximal 2000 Zeichen lang sein.")
    private String kommentar;

    public Integer getBenutzerId() {
        return benutzerId;
    }

    public void setBenutzerId(Integer benutzerId) {
        this.benutzerId = benutzerId;
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
}
