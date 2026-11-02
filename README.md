# Smart CUS & AI – Cyfrowy Hub Usług Społecznych Gminy Wieliszew
## Projekt Wielomodułowej Aplikacji Zintegrowanej (Budżet: 1 500 000 zł)

Projekt dedykowany dla mieszkańców i Centrum Usług Społecznych w Wieliszewie w oparciu o Plan Rozwoju Usług Społecznych 2026–2029, łączący e-opiekę, mikrowolontariat, bank czasu, asystę sztucznej inteligencji oraz pełną integrację z **Wieliszewską Kartą Mieszkańca (WKM)**.

---

## 📱 1. Kluczowe Moduły Systemu

1. **🤖 Wirtualny Asystent AI i Silnik Rekomendacji:**
   - Inteligentny chatbot i polski voicebot (sterowanie mową) ułatwiający obsługę seniorom i osobom z niepełnosprawnościami.
   - Algorytmy personalizujące ofertę prozdrowotną i kulturalną (np. zajęcia „Zdrowy Kręgosłup 60+”).
2. **💳 Zapisy na wydarzenia i e-Wieliszewska Karta Mieszkańca:**
   - Cyfrowy e-portfel z dynamicznym, bezpiecznym kodem QR (TOTP odświeżany co 45s) oraz emulacją NFC.
   - 1-klikowa rejestracja na warsztaty CUS i bezbiletowy wstęp na wydarzenia gminne.
3. **📍 Interaktywna Mapa Potrzeb (Mikrowolontariat):**
   - Geoprzestrzenna mapa w czasie rzeczywistym z zanonimizowanymi pinezkami potrzeb sąsiedzkich (Wieliszew, Skrzeszew, Michałów-Reginów, Łajski, Komornica, Kałuszyn, Góra).
   - Ochrona RODO – rozmycie lokalizacji do czasu podjęcia zlecenia przez wolontariusza.
4. **⏳ Lokalny Bank Czasu (Wymiana Umiejętności):**
   - Wymiana usług w formule „czas za czas” (1h pomocy = 1h w portfelu).
   - Transparentny rejestr transakcji i dopasowywanie kompetencji mieszkańców.
5. **🏆 Grywalizacja i System Nagród Mieszkańca:**
   - Wirtualne punkty i odznaki („Serce Wieliszewa”, „Aktywny Sąsiad”, „Mistrz Ekologii”).
   - Wymiana punktów na realne zniżki u lokalnych partnerów (kajaki nad Zegrzem, bilety GCK Łajski, lokalna gastronomia).
6. **🩺 Telekonsultacje i e-Wsparcie:**
   - Szyfrowane połączenia wideorozmów (WebRTC E2E) ze specjalistami CUS (psycholog, radca prawny, koordynator usług społecznych).
   - Dostępność dla osób z barierami mobilności bez konieczności dojazdu do urzędu.
7. **♻️ Moduł Współdzielenia Rzeczy i Cyrkularności:**
   - Bezpłatna wypożyczalnia sprzętu rehabilitacyjnego (wózki, balkoniki, kule).
   - Gminna Jadłodzielnia Wieliszew (walka z marnowaniem żywności) i wymiana odzieży.
8. **👁️ Tryb Seniora / WCAG 2.1 AA:**
   - Dedykowany tryb wysokiego kontrastu (żółto-czarny), powiększone fonty (+40%) i minimalne pola dotykowe 56 dp.

---

## 📂 2. Struktura Repozytorium

```
Aplikacja/
├── android_native/             # Kompletny natywny projekt Android (Kotlin + Jetpack Compose)
│   ├── app/
│   │   ├── src/main/java/pl/wieliszew/smartcus/
│   │   │   ├── MainActivity.kt
│   │   │   ├── SmartCusApp.kt
│   │   │   ├── SmartCusApplication.kt
│   │   │   ├── data/model/Models.kt
│   │   │   ├── data/repository/SmartCusRepository.kt
│   │   │   ├── modules/assistant/AiAssistantScreen.kt
│   │   │   ├── modules/card/WieliszewCardScreen.kt
│   │   │   ├── modules/map/NeedMapScreen.kt
│   │   │   ├── modules/timebank/TimeBankScreen.kt
│   │   │   ├── modules/gamification/GamificationScreen.kt
│   │   │   ├── modules/telehealth/TelehealthScreen.kt
│   │   │   ├── modules/sharing/SharingHubScreen.kt
│   │   │   └── ui/theme/ & components/
│   │   ├── build.gradle.kts
│   │   └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── settings.gradle.kts
│
├── app_preview/                # Interaktywny symulator aplikacji mobilnej w przeglądarce
│   ├── index.html              # Pixel-perfect widok smartfona z dynamicznymi modułami
│   ├── css/styles.css          # Design System Wieliszew, animacje, tryb seniora WCAG
│   └── js/app.js               # Voicebot (Web Speech API), Leaflet GPS, QR TOTP, WebRTC
│
├── docs/                       # Kompletna dokumentacja projektowa i przetargowa
│   ├── STUDIUM_WYKONALNOSCI_I_ARCHITEKTURA.md  # Feasibility study, KRI, RODO, mObywatel SSO
│   ├── KOSZTORYS_I_HARMONOGRAM_WDROZENIA.md    # Podział 1.5M zł na etapy 2026-2029 (WBS)
│   └── SPEC_INTEGRACJI_API_WKM_CUS.md          # Kontrakty REST API, tokeny TOTP, WebSockets
│
├── start_preview.py            # Skrypt uruchamiający symulator z automatyczną przeglądarką
└── README.md
```

---

## 🚀 3. Szybkie Uruchomienie i Instalacja na Telefonie

### Opcja 1: Natychmiastowa instalacja na smartfonie (PWA)
1. Uruchom serwer na komputerze:
   ```bash
   python start_preview.py
   ```
2. Na smartfonie z Androidem połączonym z domowym Wi-Fi wpisz w przeglądarce Chrome adres:
   👉 **`http://192.168.1.152:8080`**
3. Kliknij **„Dodaj do ekranu głównego” / „Zainstaluj aplikację”**. Smart CUS zainstaluje się na telefonie z oficjalną ikoną i działa w trybie pełnoekranowym!

### Opcja 2: Wygenerowanie pliku .APK
Szczegółowy przewodnik znajduje się w: [`docs/JAK_ZBUDOWAC_PLIK_APK.md`](docs/JAK_ZBUDOWAC_PLIK_APK.md).
- **W chmurze (GitHub Actions):** Gotowy plik [`.github/workflows/build-apk.yml`](.github/workflows/build-apk.yml) kompiluje `.apk` automatycznie po wrzuceniu kodu na GitHub.
- **Lokalnie w Android Studio:** Otwórz folder `android_native` ➔ menu `Build` ➔ `Build APK(s)`. Plik wyjściowy: `android_native/app/build/outputs/apk/debug/app-debug.apk`.

---

## 🛠️ 4. Otwarcie w Android Studio

1. Otwórz **Android Studio Ladybug / Koala (lub nowsze)**.
2. Wybierz **Open...** i wskaż folder: `c:\Users\jjatkowski\Desktop\Aplikacja\android_native`.
3. Gradle dokona synchronizacji zależności (Jetpack Compose BOM, Material 3, Navigation, Coroutines).
4. Uruchom aplikację na emulatorze Androida (API 26+) lub fizycznym urządzeniu.

