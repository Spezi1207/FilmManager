package com.example.filmmanagerbackend.Bewertungen;

import com.example.filmmanagerbackend.Benutzer.Benutzer;
import com.example.filmmanagerbackend.Filme.Film;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "bewertung",
        uniqueConstraints = @UniqueConstraint(columnNames = {"benutzer_id", "film_id"})
)
public class Bewertung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bewertung_id")
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "benutzer_id", nullable = false)
    private Benutzer benutzer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "film_id", nullable = false)
    private Film film;

    @Column(nullable = false)
    private Integer sterne;

    @Column(columnDefinition = "TEXT")
    private String kommentar;

    @Column(name = "bewertet_am", nullable = false)
    private LocalDateTime bewertetAm;

    @PrePersist
    @PreUpdate
    public void updateZeitstempel() {
        bewertetAm = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public Benutzer getBenutzer() {
        return benutzer;
    }

    public void setBenutzer(Benutzer benutzer) {
        this.benutzer = benutzer;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
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
}
