# Instrukcja: Jak uzyskać i zainstalować aplikację Smart CUS
## Smart CUS Wieliszew – 100% Natywna Aplikacja Kotlin (Android)

Aplikacja **Smart CUS Wieliszew** została zbudowana w 100% w języku **Kotlin** z wykorzystaniem **Jetpack Compose** oraz **Material Design 3 (Material You)**.

Poniżej znajdują się sposoby na pobranie gotowej aplikacji na smartfon lub jej skompilowanie ze źródeł.

---

## 📱 Metoda 1: Pobranie gotowego pliku .APK z GitHub Releases (Najszybsza)

Gotowy, skompilowany plik pakietu instalacyjnego dla systemu Android (wersja 8.0+) publikowany jest bezpośrednio w najnowszym oficjalnym wydaniu:
👉 **[Pobierz SmartCUS_Wieliszew.apk z GitHub Releases (Najnowsza Wersja)](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/latest/download/SmartCUS_Wieliszew.apk)**  
Strona najnowszego wydania: **[GitHub Releases v1.2.0](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/tag/v1.2.0)** (lub [Wszystkie Wydania](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases))

Plik znajduje się również lokalnie w głównym katalogu projektu pod nazwą `SmartCUS_Wieliszew.apk`.

1. Otwórz powyższy link na smartfonie, aby bezpośrednio pobrać plik instalacyjny.
2. Na smartfonie kliknij pobrany plik `.apk`.
3. Jeśli system wyświetli pytanie o instalację z nieznanych źródeł, wybierz **„Zezwalaj”**.
4. Aplikacja Smart CUS Wieliszew zainstaluje się na urządzeniu z pełnym zestawem modułów.

---

## ☁️ Metoda 2: Automatyczna kompilacja .APK w chmurze (GitHub Actions)

W projekcie przygotowano zintegrowany skrypt automatycznej budowy pliku APK: [`.github/workflows/build-apk.yml`](../.github/workflows/build-apk.yml).

1. Każdy `push` do gałęzi `main` automatycznie uruchamia proces kompilacji na serwerach GitHub Actions.
2. Środowisko z Ubuntu, JDK 17 i Android SDK 35 kompiluje kod źródłowy za pomocą Gradle.
3. Wygenerowany plik `.apk` jest automatycznie publikowany w sekcji **Releases** oraz w sekcji **Artifacts**.

---

## 💻 Metoda 3: Samodzielna kompilacja w Android Studio (Lokalnie)

1. **Pobierz środowisko Android Studio:**
   - [Pobierz Android Studio (Oficjalna strona)](https://developer.android.com/studio)
2. **Otwórz projekt:**
   - W Android Studio wybierz **Open...**
   - Wskaż główny folder repozytorium: `Aplikacja`
3. **Poczekaj na synchronizację Gradle:**
   - Środowisko automatycznie rozpozna skrypty `build.gradle.kts` i `settings.gradle.kts` oraz pobierze biblioteki Jetpack Compose BOM i Material 3.
4. **Wygeneruj plik APK:**
   - Z górnego menu wybierz: **Build** ➔ **Build Bundle(s) / APK(s)** ➔ **Build APK(s)**.
5. Plik wyjściowy znajdziesz pod ścieżką:
   `app/build/outputs/apk/debug/app-debug.apk`

---

## ⚡ Metoda 4: Kompilacja w wierszu poleceń (CLI / Gradle Wrapper)

Jeśli posiadasz zainstalowane JDK 17+ oraz Android SDK, w głównym katalogu projektu wystarczy wykonać:

```bash
# Na systemie Windows:
.\gradlew.bat assembleDebug

# Na systemie Linux / macOS:
./gradlew assembleDebug
```
