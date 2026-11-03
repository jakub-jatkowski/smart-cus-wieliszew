## 🏛️ Smart CUS Wieliszew v1.2.0 – Pełny Backend API & Naprawa Paska Statusu

Oficjalna, produkcyjna wersja aplikacji mobilnej dla mieszkańców oraz Centrum Usług Społecznych w Wieliszewie, zintegrowana z **dedykowanym serwerem Backend API (FastAPI + SQLite)** oraz w pełni rozwiązująca konflikt z systemowym paskiem stanu / zegarkiem na smartfonie.

---

### 📦 Pliki do Pobrania (Assets):
* 📱 **[`SmartCUS_Wieliszew.apk`](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/latest/download/SmartCUS_Wieliszew.apk)** – Główny pakiet instalacyjny Android APK (kompatybilny z Androidem 8.0 lub nowszym).
* 📱 **[`SmartCUS_Wieliszew_v1.2.0.apk`](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/download/v1.2.0/SmartCUS_Wieliszew_v1.2.0.apk)** – Wersjonowany pakiet instalacyjny wydania v1.2.0.
* 📦 **[`SmartCUS_Wieliszew_Source.zip`](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/download/v1.2.0/SmartCUS_Wieliszew_Source.zip)** – Pełne archiwum kodów źródłowych (Aplikacja Android + Serwer Backend API).

---

### 🌟 Kluczowe Zmiany w Wydaniu 1.2.0:

#### 1. 📱 Eliminacja Konfliktu z Górnym Paskiem Smartfona (Status Bar Insets):
* Zastosowanie natywnego mechanizmu `statusBarsPadding()` w komponencie `HeaderBanner`, dzięki czemu tytuł aplikacji, przyciski motywów, powiadomień i trybu seniora nie nachodzą na zegarek systemowy, stan baterii, ikonę Wi-Fi ani wycięcie aparatu (notch/punch-hole).
* Tło paska górnego płynnie wypełnia obszar pod paskiem statusu zgodnie z wytycznymi Edge-to-Edge w Androidzie 15 (API 35).

#### 2. 🗄️ Dedykowany Serwer Backend API (FastAPI + SQLite):
* Całkowite wyeliminowanie statycznych danych mockowych – dodano kompletny serwer REST API w katalogu `backend/` (`backend/main.py`, `backend/database.py`).
* Relacyjna baza danych **SQLite** z trwałym zapisem:
  - Zgłoszeń na Mapie Potrzeb (mikrowolontariat w sołectwach) wraz z buforem RODO,
  - Ewidencji wymiany godzin w Lokalnym Banku Czasu (1h = 1h),
  - Stanu Magazynu Sprzętu Rehabilitacyjnego z kodami odbioru,
  - Rejestru darów w całodobowej Gminnej Jadłodzielni (ul. Modlińska 65),
  - Terminarza konsultacji teleopieki i rezerwacji wizyt,
  - Profilu mieszkańca, punktów aktywności i portfela voucherów.
* Interaktywna dokumentacja Swagger UI dostępna pod adresem: `http://localhost:8000/docs`.
* Skrypty uruchomieniowe `run_server.py` oraz `run.bat` (jednym kliknięciem).

#### 3. 🌐 Pełna Integracja Sieciowa w Aplikacji Android:
* Zaimplementowano klienta HTTP `OkHttpClient` z biblioteką `Gson` (`CusApiService.kt`) realizującego asynchroniczne żądania sieciowe w tle.
* Wskaźnik statusu serwera na żywo (🟢 Połączono / 🟠 Tryb lokalny offline) w nagłówku aplikacji.
* Okno dialogowe konfiguracji adresu IP serwera CUS (`BackendSettingsDialog`) umożliwiające testowanie połączenia zarówno w emulatorze (`10.0.2.2`), jak i na fizycznym telefonie w sieci Wi-Fi.
* Architektura Offline-First: w przypadku braku połączenia z serwerem aplikacja działa stabilnie bez żadnych błędów.
