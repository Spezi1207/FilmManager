package com.example.filmmanagerbackend.Benutzer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BenutzerRepository extends JpaRepository<Benutzer, Integer> {

    Optional<Benutzer> findByBenutzernameIgnoreCase(String benutzername);

    boolean existsByBenutzernameIgnoreCase(String benutzername);

    boolean existsByEmailIgnoreCase(String email);
}
