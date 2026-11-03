# 🏛️ Smart CUS & AI – Cyfrowy Hub Usług Społecznych Gminy Wieliszew
## Oficjalna Platforma Społeczna dla Mieszkańców i Centrum Usług Społecznych
**Budżet projektu:** 1 500 000 PLN • **Horyzont:** 2026–2029 (Plan Rozwoju Usług Społecznych) • **Wersja:** 2.0 (Gotowy Produkt M3)

Projekt łączy e-opiekę, mikrowolontariat sąsiedzki, bank czasu, asystę sztucznej inteligencji oraz pełną integrację z **Wieliszewską Kartą Mieszkańca (WKM)** w oparciu o standardy **Material Design 3 (Material You)**, wytyczne dostępności cyfrowej **WCAG 2.1 AAA** oraz RODO.

---

## 📱 1. Kluczowe Moduły i Funkcjonalności

1. **🤖 Wirtualny Asystent AI i Polski Voicebot:**
   - Inteligentny asystent z obszerną bazą wiedzy o procedurach i świadczeniach CUS Wieliszew (kryteria dochodowe 2026 r., zasiłki stałe/celowe, usługi opiekuńcze, wypożyczalnia sprzętu, transport door-to-door).
   - Sterowanie mową (Web Speech API) oraz synteza mowy w języku polskim z wizualizacją fal dźwiękowych.
   - Płynna nawigacja kontekstowa z kartami rekomendacji.

2. **💳 e-Wieliszewska Karta Mieszkańca (WKM) & Portfel Voucherów:**
   - **Interaktywna Karta 3D (Flip):** Kliknięcie obraca kartę, ukazując rewers z kodem kreskowym, kontaktem ICE i podpisem.
   - **Dynamiczny Kod QR (TOTP):** Odświeżany co 45 sekund z animowaną matrycą bezpieczeństwa (ochrona przed zrzutami ekranu).
   - **Powiększenie QR na pełnym ekranie:** Ułatwione skanowanie w kasach obiektów sportowych i kulturalnych.
   - **Emulacja NFC:** Dźwiękowa i haptyczna weryfikacja zbliżeniowa.
   - **Portfel Voucherów:** Zapis pobranych nagród z unikalnymi kodami promocyjnymi.

3. **📍 Interaktywna Mapa Potrzeb (Mikrowolontariat):**
   - Geoprzestrzenna mapa OpenStreetMap z realnymi zgłoszeniami w sołectwach Gminy Wieliszew.
   - **Ochrona RODO:** Dynamiczny bufor rozmycia współrzędnych (promień 200 m).
   - Pełny cykl życia zlecenia: *Zgłoszenie ➔ Podjęcie pomocy ➔ Potwierdzenie realizacji ➔ Przyznanie punktów*.

4. **⏳ Lokalny Bank Czasu (Wymiana Umiejętności):**
   - Wymiana usług sąsiedzkich w formule „1 godzina za 1 godzinę”.
   - Interaktywny rejestr transakcji czasowych z saldem w portfelu.
   - Filtrowanie ofert mieszkańców i zapotrzebowania na pomoc.

5. **🏆 Grywalizacja i Katalog Nagród Mieszkańca:**
   - 4 poziomy zaangażowania społecznego (*Nowy Mieszkaniec ➔ Aktywny Mieszkaniec ➔ Zasłużony Sąsiad ➔ Lider Społeczności*).
   - Interaktywne odznaki („Serce Wieliszewa”, „Aktywny Sąsiad”, „Mistrz Ekologii”) z opisem warunków odblokowania.
   - Wymiana punktów na realne zniżki (kajaki nad Zalewem Zegrzyńskim, bilety do GCK Łajski, kawiarnia).

6. **🩺 Teleopieka i Konsultacje Wideo (WebRTC):**
   - Bezpieczne wideorozmowy ze specjalistami CUS (psycholog, radca prawny, koordynator usług).
   - Podgląd z kamery lokalnej (PIP) z fallbackiem do awatara.
   - System rezerwacji wizyty na kolejne dni.

7. **♻️ Magazyn Współdzielenia & Jadłodzielnia:**
   - Bezpłatne wypożyczanie sprzętu rehabilitacyjnego (wózki aluminiowe, balkoniki, kule) z generowaniem kodu odbioru.
   - Gminna Jadłodzielnia Wieliszew (ul. Modlińska 65) czynna 24/7.
   - Formularz „Przekaż dar” umożliwiający mieszkańcom dzielenie się żywnością i sprzętem.

8. **👁️ Dostępność Cyfrowa (WCAG 2.1 AAA & Material You):**
   - Dedykowany tryb seniora: żółto-czarny wysoki kontrast, powiększone fonty, obramowania 3px, minimalne pola dotykowe 56 dp.
   - Dynamiczny wybór motywów Material Design 3: Wieliszew 🌿, Bursztyn ☀️, Zalew 🌊.
   - Pełna semantyka ARIA i obsługa czytników ekranu (TalkBack / NVDA).

9. **💾 Architektura Natywna Kotlin & Reactive State:**
   - 100% natywny kod w języku **Kotlin** z deklaratywnym UI w **Jetpack Compose**.
   - Architektura oparta o wzorzec Repository, `StateFlow` oraz asynchroniczne `Kotlin Coroutines`.
   - Zgodność z najnowszymi standardami Material Design 3 (Material You).

---

## 📂 2. Struktura Repozytorium (100% Natywny Kotlin Android)

```
Aplikacja/
├── build.gradle.kts             # Główny skrypt konfiguracji projektu Gradle (Kotlin DSL)
├── settings.gradle.kts          # Konfiguracja repozytoriów i modułów (:app)
├── gradle.properties            # Parametry optymalizacji pamięci i demonów kompilatora
├── gradlew & gradlew.bat        # Skrypty kompilatora Gradle Wrapper (8.10.2)
├── gradle/wrapper/              # Dystrybucja i weryfikacja Gradle
├── SmartCUS_Wieliszew.apk       # Gotowy produkcyjny plik instalacyjny APK dla Androida
├── SmartCUS_Wieliszew_Source.zip# Kompletne archiwum kodów źródłowych
│
├── app/                         # Główny moduł aplikacji natywnej Android
│   ├── build.gradle.kts         # Zależności: Compose BOM, Material 3, Navigation, ZXing
│   ├── src/main/
│   │   ├── AndroidManifest.xml  # Manifest aplikacji z uprawnieniami (NFC, Camera, Biometrics)
│   │   ├── java/pl/wieliszew/smartcus/
│   │   │   ├── MainActivity.kt                  # Punkt wejścia z Edge-to-Edge
│   │   │   ├── SmartCusApp.kt                   # Główny szkielet UI (Scaffold, NavigationBar M3)
│   │   │   ├── SmartCusApplication.kt           # Kontener DI i inicjalizacja repozytoriów
│   │   │   ├── data/
│   │   │   │   ├── model/Models.kt              # Modele danych Kotlin (WKM, HelpRequest, Service)
│   │   │   │   └── repository/SmartCusRepository.kt # Zarządzanie stanem i baza danych w pamięci
│   │   │   ├── modules/
│   │   │   │   ├── assistant/AiAssistantScreen.kt   # Moduł Wirtualnego Doradcy AI i Voicebota
│   │   │   │   ├── card/WieliszewCardScreen.kt      # Moduł e-WKM 3D Flip & TOTP QR Code
│   │   │   │   ├── gamification/GamificationScreen.kt # Grywalizacja i katalog nagród
│   │   │   │   ├── map/NeedMapScreen.kt             # Geoprzestrzenna mapa potrzeb (RODO buffer)
│   │   │   │   ├── sharing/SharingHubScreen.kt      # Jadłodzielnia i Magazyn Sprzętu
│   │   │   │   ├── telehealth/TelehealthScreen.kt   # E-Teleopieka i wideokonsultacje
│   │   │   │   └── timebank/TimeBankScreen.kt       # Sąsiedzki Bank Czasu (1h za 1h)
│   │   │   └── ui/
│   │   │       ├── components/CommonComponents.kt   # Komponenty M3, SeniorAccessibleCard
│   │   │       └── theme/ (Color.kt, Shape.kt, Theme.kt, Type.kt) # Design System Material You
│   │   └── res/
│   │       └── values/ (colors.xml, strings.xml, themes.xml)
│
├── .github/workflows/
│   └── build-apk.yml            # CI/CD: Automatyczna kompilacja APK i publikacja do Releases
│
└── docs/                        # Kompleksowa dokumentacja techniczna i formalna
    ├── JAK_ZBUDOWAC_PLIK_APK.md
    ├── STUDIUM_WYKONALNOSCI_I_ARCHITEKTURA.md
    ├── KOSZTORYS_I_HARMONOGRAM_WDROZENIA.md
    └── SPEC_INTEGRACJI_API_WKM_CUS.md
```

---

## 🚀 3. Szybkie Uruchomienie i Dystrybucja

### Metoda 1: Pobranie i instalacja pliku .APK z GitHub Releases (Rekomendowane)
Gotowy plik instalacyjny dla systemu Android (wersja 8.0+) można pobrać bezpośrednio z najnowszego oficjalnego wydania:
👉 **[Pobierz SmartCUS_Wieliszew.apk (Najnowsze Wydanie)](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/latest/download/SmartCUS_Wieliszew.apk)**  
Strona najnowszego wydania: 👉 **[GitHub Releases v1.1.0 (Najnowsza Wersja)](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/tag/v1.1.0)** (oraz [Wszystkie Wydania](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases))

Po pobraniu na smartfon kliknij plik `.apk` i zainstaluj go jednym dotknięciem.

### Metoda 2: Otwarcie i uruchomienie w Android Studio
1. Zainstaluj **Android Studio** (wersja Koala / Ladybug lub nowsza).
2. W oknie startowym wybierz **Open** i wskaż główny katalog repozytorium `Aplikacja`.
3. Środowisko automatycznie zsynchronizuje projekt z wykorzystaniem `build.gradle.kts`.
4. Podłącz telefon z włączonym debugowaniem USB lub uruchom wbudowany emulator i naciśnij zielony przycisk **Run (Shift+F10)**.

---

## 🛠️ 4. Kompilacja ze Źródeł (Gradle / CI)

### W chmurze (GitHub Actions):
Repozytorium posiada w pełni skonfigurowany workflow [`.github/workflows/build-apk.yml`](.github/workflows/build-apk.yml). Po każdym pchnięciu do gałęzi `main`:
1. Serwery GitHub (Ubuntu + Temurin JDK 17 + Android SDK 35) kompilują czysty kod w Kotlinie.
2. Gotowy plik `.apk` jest automatycznie publikowany w sekcji **Releases** pod najnowszym tagiem wydania (np. `v1.1.0`) oraz jako artefakt kompilacji.

### Lokalnie w wierszu poleceń (Gradle):
W głównym katalogu projektu wykonaj polecenie:
```bash
./gradlew assembleDebug
```
Wygenerowany plik znajdziesz w:
`app/build/outputs/apk/debug/app-debug.apk`
