## 🏛️ Smart CUS Wieliszew v1.4.0 – Cyfrowy Standard Państwowy (mObywatel 2.0 & GovTech Benchmark)

Wydanie v1.4.0 to zwieńczenie wieloetapowego programu doskonałości aplikacji obywatelskiej, podnoszące **Smart CUS Wieliszew** do poziomu referencyjnego dla polskiego sektora publicznego. Projekt łączy wzorce **mObywatel 2.0**, wytyczne **Material Design 3 (Google)** oraz normy dostępności cyfrowej **WCAG 2.1 AAA**.

---

### 📦 Pliki do Pobrania (Assets):
* 📱 **[`SmartCUS_Wieliszew.apk`](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/latest/download/SmartCUS_Wieliszew.apk)** – Główny pakiet instalacyjny Android APK (podpisany stałym kluczem release keystore, gotowy do instalacji lub aktualizacji).
* 📱 **[`SmartCUS_Wieliszew_v1.4.0.apk`](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/download/v1.4.0/SmartCUS_Wieliszew_v1.4.0.apk)** – Wersjonowany pakiet instalacyjny wydania v1.4.0.
* 📦 **[`SmartCUS_Wieliszew_Source.zip`](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/download/v1.4.0/SmartCUS_Wieliszew_Source.zip)** – Kompletne archiwum kodów źródłowych.

---

### 🌟 Najważniejsze Wdrożenia w Wydaniu 1.4.0:

#### 1. 💳 e-Karta Mieszkańca w Standardzie Cyfrowego Portfela (Digital Wallet):
* **Dynamiczny Hologram Zabezpieczający (Security Shimmer Wave):** Ruchoma tęczowa fala światła przechodząca przez awers karty (technologia znana z mDowodu), uniemożliwiająca fałszowanie karty statycznymi zrzutami ekranu.
* **Pierścieniowy Wskaźnik TOTP (Circular Progress Ring):** Płynny, kolisty zegar odliczający ważność dynamicznego tokenu bezpieczeństwa (45s).
* **Obsługa Kart Rodzinnych (Multi-Card Profile):** Błyskawiczny przełącznik kart w ramach rodziny: Karta Główna (mieszkaniec), Karta Ucznia (dziecko) oraz Karta Seniora (rodzic/dziadek).
* **Bezpośrednie Połączenie ICE:** Przycisk szybkiego wybierania numeru ratunkowego (`ACTION_DIAL`) bezpośrednio z rewersu karty.
* **Radarowa Animacja NFC:** Wizualizacja pola elektromagnetycznego z pulsującymi falami podczas zbliżania do terminali gminnych.

#### 2. 🌲 Gminny Eko-Puls & Komunikacja Lokalna (Wieliszew Live):
* **Monitor Czystości Powietrza:** Bieżące dane ze stacji pomiarowej CUS Wieliszew (PM2.5: 8 µg/m³, PM10: 14 µg/m³, wilgotność, czystość wód Jeziora Zegrzyńskiego).
* **Tablica Odjazdów L-komunikacji:** Podgląd na żywo najbliższych odjazdów autobusów gminnych (Linia L9 → Legionowo PKP, Linia L10 → Nowy Dwór Mazowiecki, Linia L11 → Zegrze Południowe) z przystanku Urząd Gminy Wieliszew.

#### 3. 🗺️ Profesjonalna Kartografia OpenStreetMap (OSM):
* **Wektorowe Piktogramy Pinezek M3:** Nowe, ostre znaczniki potrzeb z dynamicznym kodowaniem kolorystycznym (zakupy, transport, naprawy, opieka domowa), czytelnym inicjałem oraz czerwoną aureolą pilności dla pilnych zgłoszeń.
* **Material 3 ModalBottomSheet:** Czyste, natywne arkusze dolne dla szczegółów potrzeb i filtrów kategorii (*Progressive Disclosure*).

#### 4. 🔒 Prawa Obywatela i Ochrona Danych (RODO Art. 20):
* **Eksport Danych Osobowych:** Wdrożenie prawa do przenoszenia danych (RODO Art. 20) – możliwość wygenerowania i skopiowania pełnego pakietu danych obywatela w ustrukturyzowanym formacie JSON.
* **Edycja Danych Tożsamości i ICE:** Wbudowany dialog aktualizacji danych meldunkowych i kontaktu ratunkowego z natychmiastowym zapisem.

#### 5. 🏛️ Niezawodny Backend REST API & Refaktoryzacja:
* Gotowe pliki startowe `Uruchom_Serwer_Backend.bat` oraz zoptymalizowany `backend/run.bat` z automatyczną konfiguracją zmiennej `PYTHONPATH`.
* Usunięcie ponad 1,150 zbędnych pustych linii, uporządkowanie formatowania i pełna stabilność kompilacji Gradle.
