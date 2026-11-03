## 🏛️ Smart CUS Wieliszew v1.2.1 – Kompaktowy Pasek Górny (Ultra-Sleek Top Bar) & Optymalizacja Przestrzeni Ekranu

Wydanie v1.2.1 skupia się na optymalizacji ergonomii, przestrzeni pionowej ekranu oraz maksymalizacji powierzchni roboczej dla treści modułów gminnych.

---

### 📦 Pliki do Pobrania (Assets):
* 📱 **[`SmartCUS_Wieliszew.apk`](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/latest/download/SmartCUS_Wieliszew.apk)** – Główny pakiet instalacyjny Android APK.
* 📱 **[`SmartCUS_Wieliszew_v1.2.1.apk`](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/download/v1.2.1/SmartCUS_Wieliszew_v1.2.1.apk)** – Wersjonowany pakiet instalacyjny wydania v1.2.1.
* 📦 **[`SmartCUS_Wieliszew_Source.zip`](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/download/v1.2.1/SmartCUS_Wieliszew_Source.zip)** – Pełne archiwum kodów źródłowych (Aplikacja Android + Serwer Backend API).

---

### 🌟 Kluczowe Zmiany w Wydaniu 1.2.1:

#### 1. 📱 Kompaktowy Pasek Górny (HeaderBanner):
* Zredukowano wysokość paska górnego do standardowych 52 dp (wysokość natywnego Material 3 TopAppBar) bezpośrednio pod wcięciem systemowym (`statusBarsPadding()`).
* Usunięto wielolinijkowy podtytuł, który niepotrzebnie rozszerzał nagłówek w pionie na ekranach smartfonów.
* Wprowadzono kompaktową pigułkę statusu backendu (🟢 `API` / 🟠 `Lokalny`) umieszczoną bezpośrednio obok tytułu (kliknięcie natychmiast otwiera konfigurator IP serwera).
* Przeprojektowano przełącznik trybu seniora: zamiast szerokiego przycisku z długim tekstem zastosowano ergonomiczny, okrągły przycisk dostępności **`Aa`** (32 dp), oszczędzając ponad 60 dp szerokości.
* Kompaktowe przyciski motywów i powiadomień bez zbędnych marginesów.

#### 2. 🗺️ Odzyskanie Przestrzeni w Modułach Aplikacji:
* W **Mapie Potrzeb** (`NeedMapScreen`) usunięto powieloną 70-dp kartę tytułową, zastępując ją dyskretnym, jednolinijkowym nagłówkiem ze wskaźnikiem strefy RODO 200m – dzięki temu radar i lista zgłoszeń są widoczne od razu po otwarciu ekranu.
* W **Wirtualnym Doradcy AI** (`AiAssistantScreen`) skompresowano pasek statusu asystenta głosowego z 55 dp do zgrabnych 32 dp.

#### 3. 🖥️ Usprawnienia Serwera Backend (FastAPI):
* Poprawiono zgodność skryptów `backend/run.bat` oraz `backend/run_server.py` ze środowiskiem Windows (kodowanie konsoli UTF-8 oraz bezpieczne przeładowywanie `uvicorn --reload` w procesach potomnych `SpawnProcess`).
