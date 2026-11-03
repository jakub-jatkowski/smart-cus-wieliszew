## 🏛️ Smart CUS Wieliszew v1.2.2 – Stały Klucz Wydania (Release Keystore) & Płynne Aktualizacje

Wydanie v1.2.2 wprowadza **stały, oficjalny klucz podpisywania aplikacji (Release Keystore v2/v3 Scheme)**, rozwiązując problem blokowania aktualizacji przez system Android oraz eliminując ostrzeżenia o losowych kluczach testowych.

---

### 📦 Pliki do Pobrania (Assets):
* 📱 **[`SmartCUS_Wieliszew.apk`](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/latest/download/SmartCUS_Wieliszew.apk)** – Główny pakiet instalacyjny Android APK.
* 📱 **[`SmartCUS_Wieliszew_v1.2.2.apk`](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/download/v1.2.2/SmartCUS_Wieliszew_v1.2.2.apk)** – Wersjonowany pakiet instalacyjny wydania v1.2.2.
* 📦 **[`SmartCUS_Wieliszew_Source.zip`](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/download/v1.2.2/SmartCUS_Wieliszew_Source.zip)** – Pełne archiwum kodów źródłowych.

---

### 🌟 Kluczowe Zmiany w Wydaniu 1.2.2:

#### 1. 🔑 Stały Klucz Podpisywania Aplikacji (Release Keystore):
* Dotychczas każda kompilacja w GitHub Actions była podpisywana losowym, tymczasowym kluczem maszyny wirtualnej CI, co powodowało błąd Androida: *"Pakiet powoduje konflikt z istniejącym pakietem o tej samej nazwie"* i wymuszało odinstalowanie aplikacji.
* Utworzono i zintegrowano stały magazyn kluczy `keystore/smartcus-release.jks` z oficjalnym certyfikatem dla Gminy Wieliszew / CUS (ważny 30 lat).
* Zastosowano schematy podpisu **APK Signature Scheme v1, v2 oraz v3** (SHA-256).

> [!IMPORTANT]
> **Ważna informacja przy instalacji v1.2.2:**
> Ponieważ Twoja poprzednia wersja była podpisana starym, losowym kluczem testowym z poprzedniego runnera CI, należy **jeden ostatni raz** odinstalować poprzednią wersję ze smartfona przed instalacją v1.2.2.
> **Od wersji v1.2.2 każda kolejna aktualizacja będzie instalować się bez odinstalowywania – jednym kliknięciem z zachowaniem wszystkich danych!**

#### 2. 🛡️ Ochrona Play Protect & Instalacja z Nieznanych Źródeł:
* Aplikacja podpisana stałym certyfikatem produkcyjnym nie jest już traktowana jako „losowy debug build”.
* Jak całkowicie wyciszyć monit Play Protect o skanowaniu:
  1. Przy instalacji kliknij **„Więcej szczegółów”** ➔ **„Zainstaluj mimo to”**.
  2. Jeśli Android pyta o wysłanie aplikacji do Google: w aplikacji **Sklep Google Play** ➔ kliknij ikonę profilu ➔ **Play Protect** ➔ zębatka w prawym górnym rogu ➔ wyłącz opcję: **„Ulepsz wykrywanie szkodliwych aplikacji (Wysyłaj nieznane aplikacje do Google)”**.
  3. Docelowo pełne usunięcie jakichkolwiek pytań systemu nastąpi po wdrożeniu aplikacji w Google Play Console (gdzie certyfikat Wieliszewa zostanie trwale powiązany z kontem wydawcy).
