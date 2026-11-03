## 🏛️ Smart CUS Wieliszew v1.2.3 – Perfekcyjne Skalowanie UI & Eliminacja Łamania Tekstu

Wydanie v1.2.3 rozwiązuje wszelkie problemy ze skalowaniem elementów interfejsu użytkownika, niepożądanym łamaniem etykiet na dwie linie oraz ucinaniem tekstu w przyciskach, zakładkach i odznakach na ekranach smartfonów dowolnego rozmiaru.

---

### 📦 Pliki do Pobrania (Assets):
* 📱 **[`SmartCUS_Wieliszew.apk`](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/latest/download/SmartCUS_Wieliszew.apk)** – Główny pakiet instalacyjny Android APK (podpisany stałym kluczem).
* 📱 **[`SmartCUS_Wieliszew_v1.2.3.apk`](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/download/v1.2.3/SmartCUS_Wieliszew_v1.2.3.apk)** – Wersjonowany pakiet instalacyjny wydania v1.2.3.
* 📦 **[`SmartCUS_Wieliszew_Source.zip`](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/download/v1.2.3/SmartCUS_Wieliszew_Source.zip)** – Pełne archiwum kodów źródłowych.

---

### 🌟 Kluczowe Zmiany w Wydaniu 1.2.3:

#### 1. 🔤 Eliminacja Łamania Tekstu na Dwie Linie:
* **Zakładki nawigacyjne (TabRow):** Skrócono i zoptymalizowano nazwy zakładek (*„Bank Czasu”*, *„Jadłodzielnia & Eko”*, *„Teleopieka”*, *„Punkty & Nagrody”*) wraz z parametrami `maxLines = 1` i `softWrap = false`. Zakładki mieszczą się w jednej równej linii na każdym smartfonie.
* **Dolny pasek nawigacji (NavigationBar):** Wszystkie etykiety modułów posiadają zabezpieczenie przed zawijaniem do drugiej linii.
* **Przyciski akcji w kartach:** Zoptymalizowano treść przycisków (*„Zarezerwuj bezpłatnie”*, *„Wymień pomoc (1h)”*, *„Chcę pomóc”*, *„Zapisz się”*, *„Połącz na żywo”*), dzięki czemu nigdy nie łamią się pionowo wewnątrz przycisku.

#### 2. 📏 Elastyczne Skalowanie Przycisków i Odznak:
* W komponencie `AccessibleButton` usunięto sztywną wysokość `.height(...)`, zastępując ją elastyczną minimalną wysokością `.defaultMinSize(minHeight = 48.dp..54.dp)` z zachowaniem bezpiecznego marginesu wewnętrznego.
* W odznakach `StatusBadge` wprowadzono `maxLines = 1, overflow = TextOverflow.Ellipsis`, eliminując deformację tagów statusu (np. przy długich nazwach ról specjalistów czy kategorii pomocy).
* W dymkach czatu Asystenta AI zastosowano elastyczną szerokość `fillMaxWidth(0.88f)` zamiast sztywnego ograniczenia 300 dp, dzięki czemu dymki optymalnie dopasowują się do szerokości każdego ekranu.

#### 3. 👓 Skalowanie Dostępności WCAG 2.1 AAA (Senior Mode):
* Skorygowano rozmiary `labelLarge`, `labelMedium` i `labelSmall` w skali `SeniorTypography` (15sp / 13sp / 12sp), co zapewnia znakomitą czytelność dla seniorów bez rozpychania i rozbijania kontenerów interfejsu.
