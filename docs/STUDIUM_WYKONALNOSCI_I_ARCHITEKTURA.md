# Studium Wykonalności i Architektura Systemu
## Projekt: Smart CUS & AI – Cyfrowy Hub Usług Społecznych Gminy Wieliszew
**Szacowany budżet całkowity:** 1 500 000 PLN  
**Horyzont wdrożenia:** 2026–2029 (zgodnie z Planem Rozwoju Usług Społecznych CUS Wieliszew)  
**Wersja dokumentu:** 1.0 (Stan: Listopad 2026)

---

## 1. Wprowadzenie i Cel Projektu
Centrum Usług Społecznych (CUS) w Wieliszewie realizuje zadania z zakresu integracji, profilaktyki, pomocy społecznej oraz wsparcia seniorów i osób z niepełnosprawnościami na terenie Gminy Wieliszew.
Głównym celem projektu **Smart CUS & AI** jest budowa zintegrowanego ekosystemu cyfrowego (natywna aplikacja mobilna Android + chmurowy backend mikroserwisowy + szyna integracyjna API), łączącego:
1. **Mieszkańców i beneficjentów** (szczególny nacisk na seniorów, osoby niesamodzielne i z niepełnosprawnościami).
2. **Kadrę CUS i jednostek gminnych** (pracownicy socjalni, koordynatorzy usług, psycholodzy, prawnicy).
3. **Wolontariuszy i organizacje pozarządowe (NGO)**.
4. **Lokalnych przedsiębiorców i partnerów programu Wieliszewskiej Karty Mieszkańca**.

---

## 2. Analiza Stanu Obecnego vs Stan Docelowy

| Obszar | Stan Obecny (AS-IS) | Stan Docelowy (TO-BE) |
| :--- | :--- | :--- |
| **Dostęp do usług CUS** | Wymaga wizyty osobistej lub rozmowy telefonicznej w godzinach pracy urzędu; rozproszenie ofert. | Dostęp 24/7 przez intuicyjną aplikację mobilną; asystent AI i voicebot prowadzący krok po kroku. |
| **Karta Mieszkańca** | Fizyczna karta plastikowa (często zapominana) lub odrębna prosta strona www bez integracji z zapisami. | Cyfrowa e-Karta w portfelu smartfona (dynamiczny kod QR / NFC) zintegrowana z wejściówkami na wydarzenia i zniżkami. |
| **Samopomoc i Wolontariat** | Ad-hoc, brak platformy łączącej doraźne potrzeby z wolontariuszami z danego sołectwa. | Interaktywna Mapa Potrzeb w czasie rzeczywistym z anonimizowanymi pinezkami mikrowolontariatu. |
| **Wymiana umiejętności** | Nieformalne grupy społecznościowe, brak ustrukturyzowanego rozliczania. | Lokalny Bank Czasu (wymiana kompetencji "godzina za godzinę") z cyfrowym portfelem punktów czasowych. |
| **Aktywizacja społeczna** | Tradycyjne ogłoszenia w gablotach i BIP; niski poziom partycypacji młodszych pokoleń. | Grywalizacja, poziomy zaangażowania, odznaki, nagrody u lokalnych partnerów i bilety na gminne wydarzenia. |
| **Dostępność dla osób z barierami mobilności** | Ograniczona – konieczność dojazdu do CUS lub wizyty domowej pracownika. | Szyfrowane telekonsultacje wideo / audio z psychologiem, prawnikiem i koordynatorem indywidualnych planów usług. |
| **Gospodarka cyrkularna (współdzielenie)** | Brak scentralizowanego katalogu; sprzęt rehabilitacyjny i nadwyżki często leżą niewykorzystane. | Moduł współdzielenia rzeczy: magazyn sprzętu rehabilitacyjnego, jadłodzielnia, wymiana odzieży i książek. |

---

## 3. Architektura Techniczna Systemu

### 3.1. Architektura Aplikacji Mobilnej (Android Native)
- **Język i framework:** Kotlin 2.0+, Jetpack Compose (deklaratywne UI, brak przestarzałych XML Views dla maksymalnej responsywności i płynności animacji 120Hz).
- **Architektura kodu:** Clean Architecture + MVVM + MVI (Model-View-Intent dla stanów UI) z podziałem modułowym:
  - `:app` – punkt wejścia, nawigacja (Jetpack Navigation Compose).
  - `:core:designsystem` – komponenty Material Design 3, schemat kolorów Wieliszew (zieleń #137547, złoto #DDA72A), WCAG 2.1 AA (kontrast min. 4.5:1, powiększony font, obsługa TalkBack).
  - `:core:network` – Ktor/Retrofit, WebSocket (dla powiadomień na żywo i czatu AI), mTLS.
  - `:core:database` – Room DB (szyfrowanie SQLCipher) dla pełnej pracy offline i synchronizacji dwukierunkowej.
  - Moduły domenowe:
    - `:feature:assistant` – Voicebot & LLM Orchestrator.
    - `:feature:card` – Portfel e-WKM (NFC Host Card Emulation + rotacyjny dynamiczny QR TOTP).
    - `:feature:map` – Silnik mapowy z geofencingiem sołectw Gminy Wieliszew.
    - `:feature:timebank` – Silnik transakcyjny banku czasu z ledgerem audytowym.
    - `:feature:gamification` – Silnik punktowy i grywalizacji.
    - `:feature:telehealth` – WebRTC end-to-end encrypted video/audio chat.
    - `:feature:sharing` – Katalog przedmiotów z rezerwacją i kodami odbioru.

### 3.2. Architektura Backend i Integracje
- **Środowisko:** Chmura rządowa / samorządowa (zgodna z wytycznymi KRI i CSIRT NASK), kontenery Docker / Kubernetes.
- **Backend:** Java 21 / Spring Boot 3.3 lub Kotlin Micronaut, PostgreSQL z rozszerzeniem PostGIS (dane geoprzestrzenne mapy).
- **Integracja z Wieliszewską Kartą Mieszkańca:**
  - Szyna API REST (OAuth2 / OpenID Connect).
  - Walidacja uprawnień mieszkańca (płacenie podatków w Gminie Wieliszew, meldunek).
- **Integracja z dziedzinowymi systemami CUS (np. Pomost Std / TT-Pomoc):**
  - Bezpieczna szyna integracyjna (ESB/API Gateway) z pełną anonimizacją danych wrażliwych zgodnie z RODO (brak przesyłania danych medycznych czy numerów PESEL do otwartych modułów wolontariatu).
- **Moduł AI (Wirtualny Asystent):**
  - Model językowy osadzony w architekturze RAG (Retrieval-Augmented Generation) zasilany bazą wiedzy CUS Wieliszew, BIP, aktualnościami i regulaminami świadczeń.
  - Voice-to-Text (VOT) i Text-to-Speech (TTS) w języku polskim zoptymalizowany pod aparat mowy seniorów.

---

## 4. Dostępność Cyfrowa (WCAG 2.1 AA / Ustawa z dnia 4 kwietnia 2019 r.)
Aplikacja została zaprojektowana ze szczególnym uwzględnieniem seniorów oraz osób z niepełnosprawnościami sensorycznymi:
1. **Tryb Seniora / Wysokiego Kontrastu:** Dedykowany przełącznik na pasku górnym powiększający czcionki o 40%, zwiększający grubość linii i minimalne pole dotykowe elementów do minimum 56x56 dp.
2. **Wsparcie dla Czytników Ekranu:** Wszystkie elementy posiadają `contentDescription`, prawidłowe role semantyczne w Compose i logiczny porządek nawigacji hierarchicznej.
3. **Sterowanie Głosowe:** Kluczowe czynności (zadanie pytania, wezwanie pomocy, sprawdzenie salda banku czasu) można wykonać za pomocą mowy poprzez Wirtualnego Asystenta.

---

## 5. Bezpieczeństwo i Ochrona Danych (RODO)
1. **Anonimizacja na Mapie Potrzeb:** Pinezki z prośbą o drobną pomoc (np. zrobienie zakupów, wniesienie węgla) prezentowane są z celowym rozmyciem lokalizacji (promień 150-250 metrów) i pozbawione nazwisk. Dane kontaktowe przekazywane są wolontariuszowi dopiero po weryfikacji przez koordynatora CUS i zaakceptowaniu zgłoszenia.
2. **Telekonsultacje:** Połączenia wideorozmów oparte o protokół WebRTC z szyfrowaniem DTLS-SRTP end-to-end; strumienie wideo nie są nagrywane na serwerze bez wyraźnej, odrębnej zgody i protokołu urzędowego.
3. **Szyfrowanie e-Karty:** Kod QR generowany jest z wykorzystaniem jednorazowych tokenów czasowych (TOTP), co uniemożliwia skopiowanie lub wykonanie zrzutu ekranu karty przez osoby nieuprawnione.

