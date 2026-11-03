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

9. **💾 Trwałość Danych i Tryb Offline (PWA):**
   - Pełny stan aplikacji zapisywany w `localStorage` (edycja profilu, vouchery, zgłoszenia, punkty).
   - Service Worker buforujący zasoby do pracy bez dostępu do Internetu.

---

## 📂 2. Struktura Repozytorium

```
Aplikacja/
├── Uruchom_SmartCUS.bat          # 1-klikowy launcher dla systemu Windows
├── start_preview.py             # Automatyczny serwer HTTP z wykrywaniem IP w sieci Wi-Fi
├── SmartCUS_Wieliszew.apk       # Gotowy plik instalacyjny APK dla Androida
├── SmartCUS_Wieliszew_Source.zip# Kompletne skompresowane archiwum źródeł
│
├── app_preview/                 # Produkcyjna aplikacja Web & Progressive Web App (PWA)
│   ├── index.html               # Responsywny interfejs z obsługą Mobile & Desktop Simulator
│   ├── css/styles.css           # Design System Wieliszew, animacje 3D, tryb WCAG
│   ├── js/app.js                # Silnik aplikacji: AI, TOTP QR, Leaflet GPS, WebRTC, PWA
│   ├── manifest.json            # PWA manifest z obsługą skrótów ekranowych
│   ├── sw.js                    # Service Worker (buforowanie offline, stale-while-revalidate)
│   └── icons/                   # Ikony PWA 192x192 i 512x512
│
├── android_native/              # Natywny projekt Android (Kotlin 2.0 + Jetpack Compose)
│   ├── gradlew & gradlew.bat    # Skrypty kompilatora Gradle Wrapper
│   ├── app/
│   │   ├── src/main/java/pl/wieliszew/smartcus/
│   │   │   ├── MainActivity.kt
│   │   │   ├── SmartCusApp.kt
│   │   │   ├── SmartCusApplication.kt
│   │   │   ├── data/model/Models.kt
│   │   │   ├── data/repository/SmartCusRepository.kt
│   │   │   ├── modules/ (assistant, card, map, timebank, gamification, telehealth, sharing)
│   │   │   └── ui/ (theme, components)
│   │   ├── build.gradle.kts
│   │   └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── settings.gradle.kts
│
├── .github/workflows/
│   └── build-apk.yml            # CI/CD: Automatyczna kompilacja APK w chmurze GitHub
│
└── docs/                        # Kompleksowa dokumentacja projektowa i przetargowa
    ├── JAK_ZBUDOWAC_PLIK_APK.md
    ├── STUDIUM_WYKONALNOSCI_I_ARCHITEKTURA.md
    ├── KOSZTORYS_I_HARMONOGRAM_WDROZENIA.md
    └── SPEC_INTEGRACJI_API_WKM_CUS.md
```

---

## 🚀 3. Szybkie Uruchomienie i Dystrybucja

### Metoda 1: 1-Klikowe Uruchomienie na Komputerze (Windows)
Wystarczy dwukrotnie kliknąć plik:
👉 **`Uruchom_SmartCUS.bat`**
Skrypt automatycznie uruchomi lokalny serwer i otworzy aplikację w Twojej domyślnej przeglądarce.

### Metoda 2: Uruchomienie na Smartfonie (Jako Aplikacja PWA)
1. Uruchom serwer na komputerze:
   ```bash
   python start_preview.py
   ```
2. W oknie konsoli wyświetli się dynamicznie wykryty adres IP w Twojej sieci domowej Wi-Fi, np.:
   👉 **`http://192.168.1.X:8080`**
3. Otwórz ten adres w przeglądarce Chrome na smartfonie.
4. Kliknij **„Dodaj do ekranu głównego” / „Zainstaluj aplikację”**.
5. **Efekt:** Aplikacja automatycznie dopasuje się do pełnego ekranu Twojego smartfona (bez sztucznej ramki komputera), zainstaluje oficjalną ikonę Gminy Wieliszew i będzie działać jak natywna aplikacja ze sklepu!

### Metoda 3: Pobranie i instalacja pliku .APK z GitHub Releases
Gotowy plik instalacyjny dla systemu Android (wersja 8.0+) można pobrać bezpośrednio z oficjalnego wydania:
👉 **[Pobierz SmartCUS_Wieliszew.apk (GitHub Releases)](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/download/v1.0.0/SmartCUS_Wieliszew.apk)**
lub z poziomu strony wydań:
👉 **[Strona Wydań (Releases v1.0.0)](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/tag/v1.0.0)**

Po pobraniu na smartfon kliknij plik `.apk` i zainstaluj go jednym dotknięciem.

---

## 🛠️ 4. Kompilacja ze Źródeł (Android Studio / CI)

### W chmurze (GitHub Actions):
Repozytorium posiada gotowy plik [`.github/workflows/build-apk.yml`](.github/workflows/build-apk.yml). Po wypchnięciu kodu na GitHub akcja automatycznie buduje pakiet `SmartCUS_Wieliszew_v1.0.0.apk` w sekcji *Artifacts*.

### Lokalnie w Android Studio:
1. Otwórz **Android Studio Ladybug / Koala** lub nowsze.
2. Wybierz **Open...** i wskaż folder `android_native`.
3. Z górnego menu wybierz **Build ➔ Build Bundle(s) / APK(s) ➔ Build APK(s)**.
