# Specyfikacja Integracji API: Wieliszewska Karta Mieszkańca & Systemy CUS
## Projekt Smart CUS & AI (Gmina Wieliszew)

---

## 1. Architektura Uwierzytelniania i SSO
Aplikacja mobilna Smart CUS łączy się z backendem centralnym za pośrednictwem protokołu **OAuth 2.0 / OpenID Connect**.

### Metody Logowania:
1. **Wieliszewska Karta Mieszkańca (WKM Login):**
   - Mieszkaniec podaje numer karty WKM i hasło / kod SMS.
   - Endpoint: `POST /api/v1/auth/wkm-login`
2. **Krajowy Węzeł Tożsamości (Login.gov.pl / mObywatel):**
   - Poświadczenie profilu zaufanego i weryfikacja zameldowania / płacenia podatków w Urzędzie Skarbowym w Legionowie na rzecz Gminy Wieliszew.
3. **Logowanie Anonimowe / Gość:**
   - Przeglądanie ogólnych wydarzeń i anonimizowanej Mapy Potrzeb w trybie tylko do odczytu.

---

## 2. Kluczowe Punkty Końcowe API (REST / JSON)

### 2.1. Moduł e-Karty Mieszkańca (`/api/v1/card`)
- `GET /api/v1/card/profile`
  - Zwraca dane karty: numer, status aktywności, imię, nazwisko, uprawnienia senior/rodzina 3+, termin ważności.
- `GET /api/v1/card/token`
  - Generuje dynamiczny token TOTP (Time-based One Time Password, ważny 45 sekund) służący do wygenerowania bezpiecznego kodu QR lub payloadu NFC.
  ```json
  {
    "cardNumber": "WKM-2026-08492",
    "holder": "Jan Kowalski",
    "token": "eWVsLTIwMjYtYXV0aC1zZWMtOTk4MQ==",
    "expiresInSeconds": 45,
    "seniorDiscount": true,
    "photoUrl": "https://cus.wieliszew.pl/api/cards/photo/8492.jpg"
  }
  ```

### 2.2. Moduł Interaktywnej Mapy Potrzeb (`/api/v1/map`)
- `GET /api/v1/map/needs?lat={lat}&lng={lng}&radiusKm={r}&category={cat}`
  - Zwraca zanonimizowane pinezki w rejonie Wieliszewa, Skrzeszewa, Michałowa-Reginowa, Łajsk, Komornicy, Kałuszyna, Góry.
  - Zwracana lokalizacja jest losowo rozmyta (promień 200m) ze względów ochrony RODO.
- `POST /api/v1/map/needs`
  - Dodanie nowego zapotrzebowania na wsparcie (moderowane przez koordynatora CUS).
- `POST /api/v1/map/needs/{id}/accept`
  - Wolontariusz deklaruje chęć pomocy. Po weryfikacji aplikacja otwiera bezpieczny kanał komunikacji.

### 2.3. Moduł Lokalnego Banku Czasu (`/api/v1/timebank`)
- `GET /api/v1/timebank/balance`
  - Zwraca aktualne saldo godzinowe użytkownika (np. `4.5 godziny`).
- `GET /api/v1/timebank/offers`
  - Lista ofert i poszukiwań umiejętności (np. "Nauka obsługi smartfona", "Drobne naprawy hydrauliczne", "Korepetycje").
- `POST /api/v1/timebank/transaction`
  - Potwierdzenie wykonania usługi i transfer punktów czasowych między mieszkańcami.

### 2.4. Moduł Grywalizacji i Nagród (`/api/v1/gamification`)
- `GET /api/v1/gamification/status`
  - Punkty mieszkańca, poziom (np. "Aktywny Sąsiad", "Serce Wieliszewa"), zdobyte odznaki.
- `GET /api/v1/gamification/rewards`
  - Lista nagród do odebrania (np. bilet do Gminnego Centrum Kultury, zniżka na wypożyczenie kajaka w Wieliszewie, zniżka w lokalnej piekarni).
- `POST /api/v1/gamification/redeem`
  - Wymiana punktów na voucher cyfrowy z kodem kreskowym.

### 2.5. Moduł Telekonsultacji i e-Wsparcia (`/api/v1/telehealth`)
- `GET /api/v1/telehealth/specialists`
  - Lista specjalistów CUS (psycholog, prawnik, doradca ds. osób niepełnosprawnych).
- `POST /api/v1/telehealth/session/start`
  - Inicjalizacja szyfrowanej sesji WebRTC (zwraca token WebRTC ICE/STUN/TURN).

### 2.6. Moduł Cyfrowego Współdzielenia (`/api/v1/sharing`)
- `GET /api/v1/sharing/items?category={category}`
  - Kategorie: `REHAB_EQUIPMENT` (wózki, balkoniki, kule), `FOOD_SURPLUS` (Jadłodzielnia Wieliszew), `CLOTHING` (wymiana ubrań).
- `POST /api/v1/sharing/reserve`
  - Rezerwacja przedmiotu i wygenerowanie kodu odbioru w punkcie CUS.

### 2.7. Wirtualny Asystent AI (`/api/v1/ai`)
- `POST /api/v1/ai/chat`
  - Przesłanie zapytania tekstowego lub transkrypcji mowy użytkownika.
  - Odpowiedź zawiera wygenerowaną poradę urzędową, bezpośrednie linki do modułów oraz personalizowane rekomendacje profilaktyczne.

