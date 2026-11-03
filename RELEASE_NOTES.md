## 🏛️ Smart CUS Wieliszew v1.3.0 – Nowa Architektura Informacji i Użyteczność Obywatelska

Wydanie v1.3.0 stanowi realizację całościowego planu reorganizacji i uporządkowania aplikacji opartego na badaniach użyteczności **Nielsen Norman Group (NN/g)**, wytycznych **Material Design 3 (Google)** oraz wzorcach polskich aplikacji sektora publicznego (**mObywatel 2.0**, **Gov.pl Design System**).

Aplikacja zyskała czystą, zorientowaną na cele użytkownika architekturę, eliminując problem przeładowania ekranu i zagnieżdżonych zakładek.

---

### 📦 Pliki do Pobrania (Assets):
* 📱 **[`SmartCUS_Wieliszew.apk`](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/latest/download/SmartCUS_Wieliszew.apk)** – Główny pakiet instalacyjny Android APK (podpisany stałym kluczem release keystore).
* 📱 **[`SmartCUS_Wieliszew_v1.3.0.apk`](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/download/v1.3.0/SmartCUS_Wieliszew_v1.3.0.apk)** – Wersjonowany pakiet instalacyjny wydania v1.3.0.
* 📦 **[`SmartCUS_Wieliszew_Source.zip`](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/download/v1.3.0/SmartCUS_Wieliszew_Source.zip)** – Kompletne archiwum kodów źródłowych.

---

### 🌟 Kluczowe Zmiany w Wydaniu 1.3.0:

#### 1. 🗂️ Nowy Model Nawigacji (5 głównych zadań obywatela):
* **Pulpit (`HomeScreen`)** – Oficjalny ekran startowy z najważniejszymi skrótami, powitaniem, e-kartą i komunikatami gminy.
* **e-Karta (`WieliszewCardScreen`)** – Tożsamość mieszkańca, interaktywna Karta Wieliszewska z obrotem 3D, kodem kreskowym, dynamicznym kodem QR (TOTP), NFC oraz listą oficjalnych uprawnień i ulg.
* **Pomoc (`VolunteerHubScreen`)** – Nowy, zintegrowany moduł pomocy sąsiedzkiej łączący w jednym miejscu **Mapę Pomocy** oraz **Bank Czasu**.
* **Usługi CUS (`CusServicesScreen`)** – Uporządkowane usługi instytucjonalne: **Dyżury specjalistów i teleopieka**, **Wypożyczalnia sprzętu & Jadłodzielnia** oraz **Doradca Obywatelski CUS**.
* **Profil (`ProfileScreen`)** – Dedykowane centrum tożsamości mieszkańca: dane meldunkowe, kontakt ratunkowy ICE, stan punktów i odznak, ustawienia trybu seniora (WCAG 2.1 AAA), motywy gminne oraz diagnostyka serwera backend.

#### 2. 🗺️ Przejrzysta i Niezasłonięta Mapa OpenStreetMap (OSM):
* Wyeliminowano uciążliwy dwurzędowy blok filtrów zasłaniający obszar kartograficzny.
* Wprowadzono smukły pasek z selektorem sołectw (automatyczna animacja kamery na wybrane sołectwo: Wieliszew, Łajski, Skrzeszew, Michałów-Reginów, Kałuszyn, Komornica) oraz przyciskiem filtrów kategorii.
* Filtrowanie kategorii potrzeb zostało przeniesione do eleganckiego arkusza dolnego (**Material 3 ModalBottomSheet**), zgodnie z zasadą *Progressive Disclosure*.
* Zachowano pełną ochronę prywatności mieszkańców (bufor strefy RODO 200m).

#### 3. 💳 Oficjalne Uprawnienia i Ulgi w e-Karcie Mieszkańca:
* Zamiast powielać wydarzenia z pulpitu głównego, ekran Wieliszewskiej Karty Mieszkańca prezentuje teraz realne przywileje posiadacza karty:
  - 50% zniżki na basen i halę sportową w Wieliszewie,
  - Bezpłatne wejścia na warsztaty CUS,
  - 20% zniżki na sprzęt rekreacyjny i rowery wodne,
  - Bezpłatne przejazdy komunikacją gminną (L-komunikacja).

#### 4. 👤 Dedykowane Centrum Mój Profil i Dostępność:
* Całkowite oddzielenie profilu obywatela od katalogu usług społecznych.
* Szybki podgląd numeru karty, sołectwa oraz kontaktu ICE z możliwością edycji.
* Podsumowanie aktywności społecznej: saldo godzin w Banku Czasu, zgromadzone punkty gminne oraz siatka odznak obywatelskich.
* Centralny przełącznik trybu Senior Mode (zgodność z normą WCAG 2.1 AAA z polami dotykowymi min. 54dp).
