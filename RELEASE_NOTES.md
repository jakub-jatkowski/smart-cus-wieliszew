## 🏛️ Smart CUS Wieliszew v1.2.4 – Prawdziwa Interaktywna Mapa OpenStreetMap & Material Design 3

Wydanie v1.2.4 wprowadza w pełni funkcjonalną, nowoczesną i interaktywną mapę kartograficzną **OpenStreetMap (OSM)** opartą na natywnym silniku `osmdroid` oraz wytycznych **Material Design 3 (M3)**. Zastąpiła ona wcześniejszy uproszczony schemat pełnowymiarową, responsywną mapą z gestami wielodotykowymi, dynamicznymi strefami RODO i dolnym panelem szczegółów zgłoszeń.

---

### 📦 Pliki do Pobrania (Assets):
* 📱 **[`SmartCUS_Wieliszew.apk`](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/latest/download/SmartCUS_Wieliszew.apk)** – Główny pakiet instalacyjny Android APK (podpisany oficjalnym kluczem release keystore).
* 📱 **[`SmartCUS_Wieliszew_v1.2.4.apk`](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/download/v1.2.4/SmartCUS_Wieliszew_v1.2.4.apk)** – Wersjonowany pakiet instalacyjny wydania v1.2.4.
* 📦 **[`SmartCUS_Wieliszew_Source.zip`](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/download/v1.2.4/SmartCUS_Wieliszew_Source.zip)** – Kompletne archiwum kodów źródłowych.

---

### 🌟 Najważniejsze Nowości i Zmiany w Wydaniu 1.2.4:

#### 1. 🗺️ Natywna Interaktywna Mapa OpenStreetMap (`osmdroid`):
* **Prawdziwa kartografia wektorowo-rastrowa:** Zamiast statycznego prostokąta aplikacja wyświetla rzeczywistą, płynną mapę topograficzną Gminy Wieliszew oraz okolic Jeziora Zegrzyńskiego (kafelki Mapnik / OpenStreetMap).
* **Pełna interakcja dotykowa:** Obsługa pinch-to-zoom (przybliżanie dwoma palcami), płynnego przesuwania mapy, obracania i nawigacji.
* **Płynna animacja kamery:** Wybór sołectwa w filtrze automatycznie animuje kamerę i wyśrodkowuje widok na wybranej miejscowości (`Wieliszew`, `Łajski`, `Skrzeszew`, `Michałów-Reginów`, `Kałuszyn`, `Komornica`).

#### 2. 🛡️ Strefy Prywatności RODO (200m Buffer Circles):
* Zgodnie z zasadą Privacy by Design i polskimi przepisami o ochronie danych osobowych, dokładne adresy podopiecznych nie są ujawniane.
* Każda potrzeba wolontariatu generuje na mapie 200-metrowy geometryczny okrąg RODO (`Polygon.pointsAsCircle(point, 200.0)`) z łagodnym, półprzezroczystym wypełnieniem w kolorze danej kategorii pomocy.

#### 3. 🎨 Nowoczesne Kontrolki Material Design 3 (M3):
* **Pływający pasek filtrów (Floating Filter Bar):** Elegancka półprzezroczysta pigułka M3 z podwyższeniem tonalnym u góry ekranu, umożliwiająca błyskawiczny wybór sołectwa i kategorii zgłoszenia.
* **Pływające kontrolki mapy:** Pływające przyciski szybkiego centrowania na CUS Wieliszew (`MyLocation`), przybliżania/oddalania (`+` / `-`) oraz natychmiastowego przełączania na widok listy kart.
* **Material 3 ModalBottomSheet:** Kliknięcie dowolnej pinezki na mapie wysuwa elegancki dolny arkusz ze szczegółami prośby sąsiedzkiej, informacją o punktach wolontariatu oraz przyciskiem *„Chcę pomóc”* lub *„Zakończ pomoc”*.

#### 4. 🔄 Aktualizacje „W Miejscu” (In-Place Update):
* Aplikacja podpisana jest stałym, 30-letnim oficjalnym certyfikatem release keystore (`smartcus-release.jks`), co pozwala na bezproblemową aktualizację nowej wersji z pliku APK bez konieczności odinstalowywania poprzedniej wersji.
