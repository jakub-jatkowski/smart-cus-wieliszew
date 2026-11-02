# Instrukcja: Jak uzyskać i zainstalować aplikację Smart CUS
## Smart CUS Wieliszew – Cyfrowy Hub Społeczny

Aplikacja **Smart CUS Wieliszew** została przygotowana do wielokanałowej dystrybucji, umożliwiając natychmiastowe uruchomienie bez konieczności skomplikowanych konfiguracji programistycznych.

Poniżej znajdują się **3 proste sposoby**, aby uruchomić i zainstalować aplikację na Twoim smartfonie lub komputerze.

---

## ⚡ Metoda 1: Natychmiastowa instalacja na smartfonie (PWA – Bez instalowania Android Studio)

Aplikacja w katalogu `app_preview/` została przygotowana jako w pełni certyfikowana aplikacja **PWA (Progressive Web App)** z automatyczną adaptacją do ekranu smartfona.

1. **Uruchom serwer na komputerze:**
   Kliknij dwukrotnie plik:
   👉 **`Uruchom_SmartCUS.bat`**
   *(lub wpisz w terminalu `python start_preview.py`)*.
2. **Otwórz stronę na telefonie:**
   Upewnij się, że telefon jest połączony z tą samą siecią Wi-Fi co komputer, i wpisz w przeglądarce Chrome na telefonie adres wyświetlony w oknie konsoli:
   👉 **`http://<TWOJ_ADRES_IP>:8080`**
3. **Kliknij "Zainstaluj aplikację" / "Dodaj do ekranu głównego":**
   Przeglądarka Chrome wyświetli na dole komunikat o możliwości instalacji aplikacji.
4. **Efekt:**
   Aplikacja automatycznie dopasuje się do ekranu Twojego smartfona (usuwając komputerową ramkę symulatora), doda oficjalną zielono-złotą ikonę Wieliszewa na pulpicie i będzie działać w trybie pełnoekranowym bez pasków przeglądarki!

---

## 📱 Metoda 2: Pobranie gotowego pliku .APK z GitHub Releases (Najszybsza)

Gotowy, skompilowany plik pakietu instalacyjnego dla systemu Android (wersja 8.0+) został opublikowany w oficjalnym wydaniu:
👉 **[Pobierz SmartCUS_Wieliszew.apk z GitHub Releases](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/download/v1.0.0/SmartCUS_Wieliszew.apk)**  
Strona wydania: **[GitHub Releases v1.0.0](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/tag/v1.0.0)**

Plik znajduje się również lokalnie w głównym katalogu projektu pod nazwą `SmartCUS_Wieliszew.apk`.

1. Otwórz powyższy link na smartfonie, aby bezpośrednio pobrać plik instalacyjny.
2. Na smartfonie kliknij pobrany plik `.apk`.
3. Jeśli system wyświetli pytanie o instalację z nieznanych źródeł, wybierz **„Zezwalaj”**.
4. Aplikacja Smart CUS Wieliszew zainstaluje się na urządzeniu z pełnym zestawem modułów.

---

## ☁️ Metoda 3: Automatyczna kompilacja .APK w chmurze (GitHub Actions)

W projekcie przygotowano zaktualizowany skrypt automatycznej budowy pliku APK: [`.github/workflows/build-apk.yml`](../.github/workflows/build-apk.yml).

1. Umieść repozytorium na platformie GitHub.
2. Przejdź do zakładki **Actions** w repozytorium GitHub.
3. Serwery GitHub (z Ubuntu, JDK 17 i Android SDK 35) automatycznie skompilują kod źródłowy za pomocą Gradle.
4. Po zakończeniu zadania (ok. 2–3 minuty) w sekcji **Artifacts** pobierzesz świeżo wygenerowany plik `SmartCUS_Wieliszew_v1.0.0.apk`.

---

## 💻 Metoda 4: Samodzielna kompilacja w Android Studio (Lokalnie)

1. **Pobierz środowisko Android Studio:**
   - [Pobierz Android Studio (Oficjalna strona)](https://developer.android.com/studio)
2. **Otwórz projekt:**
   - W Android Studio wybierz **Open...**
   - Wskaż folder: `android_native/`
3. **Poczekaj na synchronizację Gradle:**
   - Środowisko automatycznie pobierze wymagane biblioteki (Jetpack Compose BOM, Material 3, Android SDK 35).
4. **Wygeneruj plik APK:**
   - Z górnego menu wybierz: **Build** ➔ **Build Bundle(s) / APK(s)** ➔ **Build APK(s)**.
5. Plik wyjściowy znajdziesz pod ścieżką:
   `android_native/app/build/outputs/apk/debug/app-debug.apk`
