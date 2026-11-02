# Instrukcja: Jak uzyskać i zainstalować plik .APK
## Smart CUS Wieliszew – Cyfrowy Hub Społeczny

Plik **.apk** (Android Application Package) to skompilowany i podpisany cyfrowo pakiet instalacyjny dla systemu Android. Kod źródłowy aplikacji znajduje się w folderze [`android_native/`](file:///c:/Users/jjatkowski/Desktop/Aplikacja/android_native).

Poniżej znajdują się **3 proste sposoby**, aby uruchomić i zainstalować aplikację na Twoim smartfonie.

---

## ⚡ Metoda 1: Natychmiastowa instalacja na smartfonie (PWA – Bez instalowania Android Studio)

Aplikacja w katalogu `app_preview/` została przygotowana jako w pełni certyfikowana aplikacja **PWA (Progressive Web App)** z obsługą instalacji na pulpicie smartfona.

1. **Uruchom serwer na komputerze:**
   W terminalu na komputerze uruchom:
   ```bash
   python start_preview.py
   ```
2. **Otwórz stronę na telefonie:**
   Upewnij się, że telefon jest połączony z tą samą siecią Wi-Fi co komputer, i wpisz w przeglądarce Chrome na telefonie adres Twojego komputera:
   👉 **`http://192.168.1.152:8080`**
3. **Kliknij "Zainstaluj aplikację":**
   Przeglądarka Chrome wyświetli na dole komunikat: **„Dodaj Smart CUS do ekranu głównego”** lub **„Zainstaluj aplikację”**.
4. **Efekt:**
   Aplikacja pojawi się w menu Twojego telefonu z oficjalną zielono-złotą ikoną Wieliszewa, otwiera się na pełnym ekranie (bez pasków przeglądarki) i działa jak klasyczna aplikacja Android!

---

## ☁️ Metoda 2: Automatyczna kompilacja .APK w chmurze (GitHub Actions – 2 minuty)

W projekcie przygotowano gotowy skrypt automatycznej budowy pliku APK: [`.github/workflows/build-apk.yml`](file:///c:/Users/jjatkowski/Desktop/Aplikacja/.github/workflows/build-apk.yml).

1. Umieść folder projektu w repozytorium GitHub (publicznym lub prywatnym).
2. Wejdź w zakładkę **Actions** na GitHubie.
3. Serwery GitHub (z preinstalowanym Ubuntu, JDK 17 i Android SDK 35) automatycznie skompilują kod źródłowy za pomocą Gradle.
4. Po zakończeniu zadania (ok. 2–3 minuty) w sekcji **Artifacts** pobierzesz gotowy plik:
   📦 **`SmartCUS-Wieliszew-Debug-APK.zip`**  
   zawierający instalator `app-debug.apk`.
5. Prześlij plik `.apk` na telefon i kliknij go, aby zainstalować.

---

## 💻 Metoda 3: Samodzielna kompilacja w Android Studio (Lokalnie)

Jeśli chcesz skompilować plik `.apk` lokalnie na komputerze programistycznym:

1. **Pobierz i zainstaluj darmowe środowisko Android Studio:**
   - [Pobierz Android Studio (Oficjalna strona)](https://developer.android.com/studio)
2. **Otwórz projekt:**
   - W Android Studio wybierz **Open...**
   - Wskaż folder: `c:\Users\jjatkowski\Desktop\Aplikacja\android_native`
3. **Poczekaj na synchronizację Gradle:**
   - Android Studio automatycznie pobierze wymagane biblioteki (Jetpack Compose, Material 3, Android SDK 35).
4. **Wygeneruj plik APK:**
   - Z górnego menu wybierz: **Build** ➔ **Build Bundle(s) / APK(s)** ➔ **Build APK(s)**.
5. **Lokalizacja gotowego pliku APK:**
   Po zakończeniu kompilacji plik znajdziesz pod ścieżką:
   `android_native\app\build\outputs\apk\debug\app-debug.apk`
6. Prześlij go na telefon (kablem USB, e-mailem lub Dyskiem Google) i kliknij **Zainstaluj**.

