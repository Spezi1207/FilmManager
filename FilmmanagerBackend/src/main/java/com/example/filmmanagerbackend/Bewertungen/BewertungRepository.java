package com.example.filmmanagerbackend.Bewertungen;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BewertungRepository extends JpaRepository<Bewertung, Integer> {

    List<Bewertung> findByFilm_IdOrderByBewertetAmDesc(Integer filmId);

    Optional<Bewertung> findByFilm_IdAndBenutzer_Id(Integer filmId, Integer benutzerId);

    void deleteByFilm_Id(Integer filmId);
}
