import { CommonModule } from '@angular/common';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';

interface Hinweis {
  type: 'success' | 'error';
  text: string;
}

interface Benutzer {
  id: number;
  benutzername: string;
  email: string;
  geburtsdatum: string;
  geschlecht: 'M' | 'W' | 'D';
  erstelltAm: string;
  aktiv: boolean;
  administrator: boolean;
}

interface AuthResponse {
  message: string;
  benutzer: Benutzer;
}

interface Film {
  id: number;
  titel: string;
  erscheinungsdatum: string;
  filmlaengeMin: number;
  filmlaengeAnzeige: string;
  kategorie: string;
  kurzbeschreibung: string | null;
  titelbildPfad: string | null;
  hinzugefuegtAm: string;
  durchschnittsbewertung: number;
  bewertungenAnzahl: number;
}

interface Bewertung {
  id: number;
  benutzerId: number;
  benutzername: string;
  filmId: number;
  sterne: number;
  kommentar: string | null;
  bewertetAm: string;
  eigeneBewertung: boolean;
}

interface BewertungForm {
  sterne: number;
  kommentar: string;
}

@Component({
  selector: 'app-root',
  imports: [CommonModule, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  protected readonly apiUrl = 'http://localhost:8080/api';

  protected hinweis: Hinweis | null = null;
  protected currentUser: Benutzer | null = null;
  protected films: Film[] = [];
  protected ratingsByFilm: Record<number, Bewertung[]> = {};
  protected ratingFormsByFilm: Record<number, BewertungForm> = {};
  protected selectedFilmId: number | null = null;
  protected editingFilmId: number | null = null;
  protected loadingFilms = false;
  protected brokenPosterIds = new Set<number>();

  protected filters = {
    suche: '',
    kategorie: '',
    jahr: '',
    sortierung: 'titel'
  };

  protected registerForm = {
    benutzername: '',
    email: '',
    passwort: '',
    geburtsdatum: '',
    geschlecht: 'D' as 'M' | 'W' | 'D'
  };

  protected loginForm = {
    benutzername: '',
    passwort: ''
  };

  protected profileForm = {
    email: '',
    neuesPasswort: ''
  };

  protected adminForm = {
    userId: ''
  };

  protected filmForm = {
    titel: '',
    erscheinungsdatum: '',
    filmlaengeMin: 120,
    kategorie: '',
    kurzbeschreibung: '',
    titelbildPfad: ''
  };

  constructor(private readonly http: HttpClient) {}

  ngOnInit(): void {
    this.loadFilms();
  }

  protected get kategorien(): string[] {
    return [...new Set(this.films.map((film) => film.kategorie))].sort((a, b) =>
      a.localeCompare(b, 'de')
    );
  }

  protected get jahre(): number[] {
    return [...new Set(this.films.map((film) => Number(film.erscheinungsdatum.slice(0, 4))))]
      .sort((a, b) => b - a);
  }

  protected get anzahlBewertungen(): number {
    return this.films.reduce((summe, film) => summe + film.bewertungenAnzahl, 0);
  }

  protected get durchschnittLabel(): string {
    const filmeMitBewertung = this.films.filter((film) => film.bewertungenAnzahl > 0);
    if (!filmeMitBewertung.length) {
      return '0.0';
    }

    const wert =
      filmeMitBewertung.reduce((summe, film) => summe + film.durchschnittsbewertung, 0) /
      filmeMitBewertung.length;

    return wert.toFixed(1);
  }

  protected registrieren(): void {
    this.http
      .post<AuthResponse>(`${this.apiUrl}/auth/register`, this.registerForm)
      .subscribe({
        next: (response) => {
          this.currentUser = response.benutzer;
          this.loginForm.benutzername = response.benutzer.benutzername;
          this.loginForm.passwort = '';
          this.registerForm = {
            benutzername: '',
            email: '',
            passwort: '',
            geburtsdatum: '',
            geschlecht: 'D'
          };
          this.syncProfileForm();
          this.setHinweis('success', response.message);
        },
        error: (error) => this.setHinweis('error', this.extractError(error, 'Registrierung fehlgeschlagen.'))
      });
  }

  protected anmelden(): void {
    this.http
      .post<AuthResponse>(`${this.apiUrl}/auth/login`, this.loginForm)
      .subscribe({
        next: (response) => {
          this.currentUser = response.benutzer;
          this.syncProfileForm();
          this.setHinweis('success', response.message);
          if (this.selectedFilmId !== null) {
            this.loadRatings(this.selectedFilmId);
          }
        },
        error: (error) => this.setHinweis('error', this.extractError(error, 'Anmeldung fehlgeschlagen.'))
      });
  }

  protected abmelden(): void {
    this.currentUser = null;
    this.profileForm = { email: '', neuesPasswort: '' };
    this.adminForm.userId = '';
    this.setHinweis('success', 'Sie wurden abgemeldet.');
  }

  protected profilSpeichern(): void {
    if (!this.currentUser) {
      return;
    }

    const payload = {
      email: this.profileForm.email,
      neuesPasswort: this.profileForm.neuesPasswort.trim() || null
    };

    this.http.put<Benutzer>(`${this.apiUrl}/users/${this.currentUser.id}`, payload).subscribe({
      next: (benutzer) => {
        this.currentUser = benutzer;
        this.syncProfileForm();
        this.setHinweis('success', 'Profil wurde aktualisiert.');
      },
      error: (error) => this.setHinweis('error', this.extractError(error, 'Profil konnte nicht gespeichert werden.'))
    });
  }

  protected benutzerDeaktivieren(): void {
    if (!this.currentUser || !this.currentUser.administrator || !this.adminForm.userId) {
      return;
    }

    this.http
      .patch<Benutzer>(
        `${this.apiUrl}/users/${this.adminForm.userId}/deactivate`,
        {},
        { params: new HttpParams().set('actorId', this.currentUser.id) }
      )
      .subscribe({
        next: () => {
          this.adminForm.userId = '';
          this.setHinweis('success', 'Benutzerkonto wurde deaktiviert.');
        },
        error: (error) => this.setHinweis('error', this.extractError(error, 'Benutzer konnte nicht deaktiviert werden.'))
      });
  }

  protected loadFilms(): void {
    let params = new HttpParams();

    if (this.filters.suche.trim()) {
      params = params.set('suche', this.filters.suche.trim());
    }

    if (this.filters.kategorie) {
      params = params.set('kategorie', this.filters.kategorie);
    }

    if (this.filters.jahr) {
      params = params.set('jahr', this.filters.jahr);
    }

    if (this.filters.sortierung) {
      params = params.set('sortierung', this.filters.sortierung);
    }

    this.loadingFilms = true;
    this.http.get<Film[]>(`${this.apiUrl}/films`, { params }).subscribe({
      next: (films) => {
        this.films = films;
        this.loadingFilms = false;
      },
      error: (error) => {
        this.loadingFilms = false;
        this.setHinweis('error', this.extractError(error, 'Filme konnten nicht geladen werden.'));
      }
    });
  }

  protected filterZuruecksetzen(): void {
    this.filters = {
      suche: '',
      kategorie: '',
      jahr: '',
      sortierung: 'titel'
    };
    this.loadFilms();
  }

  protected filmSpeichern(): void {
    const payload = {
      ...this.filmForm,
      filmlaengeMin: Number(this.filmForm.filmlaengeMin),
      kurzbeschreibung: this.filmForm.kurzbeschreibung.trim() || null,
      titelbildPfad: this.filmForm.titelbildPfad.trim() || null
    };

    const request =
      this.editingFilmId === null
        ? this.http.post<Film>(`${this.apiUrl}/films`, payload)
        : this.http.put<Film>(`${this.apiUrl}/films/${this.editingFilmId}`, payload);

    request.subscribe({
      next: () => {
        this.setHinweis(
          'success',
          this.editingFilmId === null ? 'Film wurde angelegt.' : 'Film wurde aktualisiert.'
        );
        this.resetFilmForm();
        this.loadFilms();
      },
      error: (error) => this.setHinweis('error', this.extractError(error, 'Film konnte nicht gespeichert werden.'))
    });
  }

  protected filmBearbeiten(film: Film): void {
    this.editingFilmId = film.id;
    this.filmForm = {
      titel: film.titel,
      erscheinungsdatum: film.erscheinungsdatum,
      filmlaengeMin: film.filmlaengeMin,
      kategorie: film.kategorie,
      kurzbeschreibung: film.kurzbeschreibung ?? '',
      titelbildPfad: film.titelbildPfad ?? ''
    };
  }

  protected filmLoeschen(filmId: number): void {
    if (!window.confirm('Soll dieser Film wirklich geloescht werden?')) {
      return;
    }

    this.http.delete(`${this.apiUrl}/films/${filmId}`).subscribe({
      next: () => {
        delete this.ratingsByFilm[filmId];
        delete this.ratingFormsByFilm[filmId];
        if (this.selectedFilmId === filmId) {
          this.selectedFilmId = null;
        }
        this.setHinweis('success', 'Film wurde geloescht.');
        this.loadFilms();
      },
      error: (error) => this.setHinweis('error', this.extractError(error, 'Film konnte nicht geloescht werden.'))
    });
  }

  protected filmFormZuruecksetzen(): void {
    this.resetFilmForm();
  }

  protected toggleBewertungen(filmId: number): void {
    if (this.selectedFilmId === filmId) {
      this.selectedFilmId = null;
      return;
    }

    this.selectedFilmId = filmId;
    this.ensureRatingForm(filmId);
    this.loadRatings(filmId);
  }

  protected bewertungSpeichern(filmId: number): void {
    if (!this.currentUser) {
      this.setHinweis('error', 'Bitte melden Sie sich an, um eine Bewertung abzugeben.');
      return;
    }

    const form = this.ratingFormsByFilm[filmId];
    if (!form) {
      return;
    }

    this.http
      .post<Bewertung>(`${this.apiUrl}/films/${filmId}/bewertungen`, {
        benutzerId: this.currentUser.id,
        sterne: Number(form.sterne),
        kommentar: form.kommentar.trim() || null
      })
      .subscribe({
        next: () => {
          this.setHinweis('success', 'Bewertung wurde gespeichert.');
          this.loadRatings(filmId);
          this.loadFilms();
        },
        error: (error) => this.setHinweis('error', this.extractError(error, 'Bewertung konnte nicht gespeichert werden.'))
      });
  }

  protected eigeneBewertungLoeschen(filmId: number): void {
    if (!this.currentUser) {
      return;
    }

    this.http.delete(`${this.apiUrl}/films/${filmId}/bewertungen/${this.currentUser.id}`).subscribe({
      next: () => {
        this.ratingFormsByFilm[filmId] = { sterne: 5, kommentar: '' };
        this.setHinweis('success', 'Bewertung wurde geloescht.');
        this.loadRatings(filmId);
        this.loadFilms();
      },
      error: (error) => this.setHinweis('error', this.extractError(error, 'Bewertung konnte nicht geloescht werden.'))
    });
  }

  protected onPosterSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];

    if (!file) {
      return;
    }

    const reader = new FileReader();
    reader.onload = () => {
      this.filmForm.titelbildPfad = String(reader.result ?? '');
    };
    reader.readAsDataURL(file);
  }

  protected removePoster(): void {
    this.filmForm.titelbildPfad = '';
  }

  protected markPosterAsBroken(filmId: number): void {
    this.brokenPosterIds.add(filmId);
  }

  protected shouldShowPoster(film: Film): boolean {
    return Boolean(film.titelbildPfad) && !this.brokenPosterIds.has(film.id);
  }

  protected badgeForFilm(film: Film): string {
    if (film.durchschnittsbewertung >= 4.5) {
      return 'Top bewertet';
    }

    if (film.bewertungenAnzahl === 0) {
      return 'Noch ohne Bewertung';
    }

    return `${film.bewertungenAnzahl} Bewertung${film.bewertungenAnzahl === 1 ? '' : 'en'}`;
  }

  private loadRatings(filmId: number): void {
    let params = new HttpParams();

    if (this.currentUser) {
      params = params.set('currentUserId', this.currentUser.id);
    }

    this.http
      .get<Bewertung[]>(`${this.apiUrl}/films/${filmId}/bewertungen`, { params })
      .subscribe({
        next: (bewertungen) => {
          this.ratingsByFilm[filmId] = bewertungen;
          const eigeneBewertung = bewertungen.find((bewertung) => bewertung.eigeneBewertung);
          this.ratingFormsByFilm[filmId] = {
            sterne: eigeneBewertung?.sterne ?? 5,
            kommentar: eigeneBewertung?.kommentar ?? ''
          };
        },
        error: (error) => this.setHinweis('error', this.extractError(error, 'Bewertungen konnten nicht geladen werden.'))
      });
  }

  private ensureRatingForm(filmId: number): void {
    if (!this.ratingFormsByFilm[filmId]) {
      this.ratingFormsByFilm[filmId] = { sterne: 5, kommentar: '' };
    }
  }

  private resetFilmForm(): void {
    this.editingFilmId = null;
    this.filmForm = {
      titel: '',
      erscheinungsdatum: '',
      filmlaengeMin: 120,
      kategorie: '',
      kurzbeschreibung: '',
      titelbildPfad: ''
    };
  }

  private syncProfileForm(): void {
    this.profileForm = {
      email: this.currentUser?.email ?? '',
      neuesPasswort: ''
    };
  }

  private setHinweis(type: 'success' | 'error', text: string): void {
    this.hinweis = { type, text };
  }

  private extractError(error: unknown, fallback: string): string {
    if (error instanceof HttpErrorResponse) {
      if (error.error?.errors && typeof error.error.errors === 'object') {
        return Object.values(error.error.errors as Record<string, string>).join(' ');
      }

      if (typeof error.error?.message === 'string') {
        return error.error.message;
      }
    }

    return fallback;
  }
}
