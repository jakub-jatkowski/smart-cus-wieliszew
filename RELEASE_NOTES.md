## 🏛️ Smart CUS Wieliszew v1.4.1 – Offline-First Persistence & Pełna Kartografia Sołectw

Wydanie v1.4.1 stanowi bezpośrednie rozszerzenie wersji v1.4.0 (mObywatel 2.0 Benchmark) o pełną architekturę **Offline-First** opartą na pamięci urządzenia (`SharedPreferences`), gwarantującą niezawodne działanie bez konieczności stałego połączenia z siecią, oraz kompletną obsługę wszystkich sołectw Gminy Wieliszew.

---

### 📦 Pliki do Pobrania (Assets):
* 📱 **[`SmartCUS_Wieliszew.apk`](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/latest/download/SmartCUS_Wieliszew.apk)** – Główny pakiet instalacyjny Android APK (podpisany stałym kluczem release keystore, płynna aktualizacja).
* 📱 **[`SmartCUS_Wieliszew_v1.4.1.apk`](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/download/v1.4.1/SmartCUS_Wieliszew_v1.4.1.apk)** – Wersjonowany pakiet instalacyjny wydania v1.4.1.
* 📦 **[`SmartCUS_Wieliszew_Source.zip`](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/download/v1.4.1/SmartCUS_Wieliszew_Source.zip)** – Kompletne archiwum kodów źródłowych.

---

### 🌟 Kluczowe Udoskonalenia w Wydaniu 1.4.1:

#### 1. 💾 Niezawodna Persystencja Lokalna (Offline-First):
* **Zachowanie Ustawień Senior Mode:** Włączony tryb dostępności WCAG 2.1 AAA jest natychmiast zapamiętywany w pamięci trwałej urządzenia i przywracany przy ponownym uruchomieniu aplikacji.
* **Autozapis Danych Mieszkańca:** Zmiana danych osobowych, sołectwa, kontaktu ratunkowego ICE, salda godzin w Banku Czasu oraz punktów wolontariatu jest w czasie rzeczywistym synchronizowana z lokalną bazą `SharedPreferences`.
* **Trwała Konfiguracja API:** Zdefiniowany adres serwera CUS API jest zapamiętywany na stałe, co ułatwia pracę zarówno w sieci lokalnej (LAN/WiFi), jak i poza nią.

#### 2. 🗺️ Kompletna Siatka Kartograficzna Wszystkich 12 Sołectw Gminy Wieliszew:
* Dodano pełny wykaz sołectw wraz z precyzyjnymi współrzędnymi GPS:
  - **Wieliszew, Łajski, Skrzeszew, Michałów-Reginów, Kałuszyn, Komornica, Krubin, Janówek Pierwszy, Poddębie, Sikory, Topolina, Olszewnica Stara, Olszewnica Nowa**.
* Płynna animacja kamery OpenStreetMap (`animateTo`) na każde wybrane sołectwo.

#### 3. 💳 Cyfrowy Portfel & Zabezpieczenia:
* Pełna integracja dynamicznego hologramu falowego (Security Shimmer Wave), pierścieniowego odliczania tokenu TOTP (Circular Progress Ring) oraz kart rodzinnych.
* Bezpośrednie połączenie ICE jednym dotknięciem na rewersie karty.
