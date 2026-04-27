package com.example.filmmanagerbackend.Benutzer;

import com.example.filmmanagerbackend.Common.ApiException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class BenutzerService {

    private final BenutzerRepository benutzerRepository;

    public BenutzerService(BenutzerRepository benutzerRepository) {
        this.benutzerRepository = benutzerRepository;
    }

    public BenutzerResponse registrieren(BenutzerRegistrationRequest request) {
        if (benutzerRepository.existsByBenutzernameIgnoreCase(request.getBenutzername())) {
            throw new ApiException(HttpStatus.CONFLICT, "Der Benutzername ist bereits vergeben.");
        }

        if (benutzerRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new ApiException(HttpStatus.CONFLICT, "Die E-Mail-Adresse ist bereits vergeben.");
        }

        Benutzer benutzer = new Benutzer();
        benutzer.setBenutzername(request.getBenutzername().trim());
        benutzer.setEmail(request.getEmail().trim().toLowerCase());
        benutzer.setPasswortHash(BCrypt.hashpw(request.getPasswort(), BCrypt.gensalt()));
        benutzer.setGeburtsdatum(request.getGeburtsdatum());
        benutzer.setGeschlecht(request.getGeschlecht());
        benutzer.setAktiv(true);

        return BenutzerResponse.fromEntity(benutzerRepository.save(benutzer));
    }

    public BenutzerResponse anmelden(LoginRequest request) {
        Benutzer benutzer = benutzerRepository.findByBenutzernameIgnoreCase(request.getBenutzername().trim())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Benutzername oder Passwort ist falsch."));

        if (!benutzer.isAktiv()) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Dieses Benutzerkonto ist deaktiviert.");
        }

        if (!BCrypt.checkpw(request.getPasswort(), benutzer.getPasswortHash())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Benutzername oder Passwort ist falsch.");
        }

        return BenutzerResponse.fromEntity(benutzer);
    }

    public BenutzerResponse getBenutzer(Integer id) {
        return BenutzerResponse.fromEntity(loadBenutzer(id));
    }

    public List<BenutzerResponse> getAlleBenutzer(Integer actorId) {
        assertAdmin(actorId);

        return benutzerRepository.findAll().stream()
                .sorted(Comparator.comparing(Benutzer::getBenutzername, String.CASE_INSENSITIVE_ORDER))
                .map(BenutzerResponse::fromEntity)
                .toList();
    }

    public Benutzer loadBenutzer(Integer id) {
        return benutzerRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Benutzer wurde nicht gefunden."));
    }

    public BenutzerResponse profilAktualisieren(Integer id, BenutzerUpdateRequest request) {
        Benutzer benutzer = loadBenutzer(id);

        String normalizedEmail = request.getEmail().trim().toLowerCase();
        if (!normalizedEmail.equalsIgnoreCase(benutzer.getEmail())
                && benutzerRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new ApiException(HttpStatus.CONFLICT, "Die E-Mail-Adresse ist bereits vergeben.");
        }

        benutzer.setEmail(normalizedEmail);

        if (request.getNeuesPasswort() != null && !request.getNeuesPasswort().isBlank()) {
            benutzer.setPasswortHash(BCrypt.hashpw(request.getNeuesPasswort(), BCrypt.gensalt()));
        }

        return BenutzerResponse.fromEntity(benutzerRepository.save(benutzer));
    }

    public BenutzerResponse kontoDeaktivieren(Integer zielBenutzerId, Integer actorId) {
        assertAdmin(actorId);

        if (zielBenutzerId.equals(actorId)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Das eigene Administratorkonto kann nicht deaktiviert werden.");
        }

        Benutzer benutzer = loadBenutzer(zielBenutzerId);
        if (!benutzer.isAktiv()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Dieses Benutzerkonto ist bereits deaktiviert.");
        }
        benutzer.setAktiv(false);
        return BenutzerResponse.fromEntity(benutzerRepository.save(benutzer));
    }

    private void assertAdmin(Integer actorId) {
        Benutzer actor = loadBenutzer(actorId);
        if (!"admin".equalsIgnoreCase(actor.getBenutzername())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Nur ein Administrator darf diese Aktion ausfuehren.");
        }
    }
}
