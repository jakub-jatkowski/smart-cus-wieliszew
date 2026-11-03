# 🏛️ Smart CUS Wieliszew – Serwer Backend API (GovTech 2.0)

Oficjalny serwer API oraz relacyjna baza danych **SQLite** dla aplikacji mobilnej **Smart CUS Wieliszew**.

## 🚀 Szybkie Uruchomienie Serwera

### Wymagania:
* Python 3.10+ (wraz z pakietami `fastapi`, `uvicorn`, `pydantic`)

### Uruchomienie:
W głównym katalogu projektu uruchom:
```bash
python backend/run_server.py
```
lub w systemie Windows dwukrotnie kliknij plik:
```
backend/run.bat
```

Serwer uruchomi się pod adresem:
* **Główny adres API:** `http://localhost:8000` (lub `http://0.0.0.0:8000`)
* **Dla emulatora Android:** `http://10.0.2.2:8000`
* **Interaktywna dokumentacja Swagger UI:** `http://localhost:8000/docs`
* **Dokumentacja ReDoc:** `http://localhost:8000/redoc`

---

## 📡 Dostępne Punkty Końcowe (Endpoints):

### 1. System & Health
* `GET /` – Status systemu, informacje o CUS Wieliszew.
* `GET /api/health` – Sprawdzenie stanu serwera i bazy danych SQLite.

### 2. e-Karta Mieszkańca (WKM) & Profil
* `GET /api/resident` – Pobranie profilu mieszkańca, numeru karty i salda.
* `PUT /api/resident` – Aktualizacja danych mieszkańca i kontaktu ratunkowego ICE.

### 3. Powiadomienia Gminne
* `GET /api/notifications` – Lista komunikatów i alertów IMGW/CUS.
* `POST /api/notifications/{id}/read` – Oznaczenie komunikatu jako przeczytany.

### 4. Mapa Potrzeb & Mikrowolontariat
* `GET /api/need-pins?village={solectwo}&category={kategoria}` – Lista aktywnych pinezek.
* `POST /api/need-pins` – Zgłoszenie nowej potrzeby pomocy sąsiedzkiej.
* `POST /api/need-pins/{id}/accept` – Podjęcie pomocy przez wolontariusza (+50 pkt).
* `POST /api/need-pins/{id}/complete` – Zakończenie realizacji zadania.

### 5. Bank Czasu (1h = 1h)
* `GET /api/time-bank?type={OFFERING|REQUESTING}` – Lista ofert i zapotrzebowań.
* `POST /api/time-bank` – Dodanie nowego wpisu do banku czasu.
* `POST /api/time-bank/{id}/exchange` – Potwierdzenie wymiany godzinowej i rozliczenie salda.

### 6. Wypożyczalnia Sprzętu Rehabilitacyjnego
* `GET /api/equipment` – Lista dostępnego sprzętu (wózki, balkoniki, kule).
* `POST /api/equipment/{id}/reserve` – Rezerwacja sprzętu i wygenerowanie unikalnego kodu odbioru (`CUS-W-2026-XXXX`).

### 7. Magazyn Współdzielenia & Gminna Jadłodzielnia
* `GET /api/jadlodzielnia` – Stan produktów w całodobowej lodówce gminnej (ul. Modlińska 65).
* `POST /api/jadlodzielnia/donate` – Rejestracja nowego daru żywnościowego.

### 8. Teleopieka & Konsultacje Specjalistyczne
* `GET /api/telehealth/specialists` – Lista dyżurujących specjalistów (psycholog, prawnik, koordynator).
* `POST /api/telehealth/book` – Rezerwacja terminu konsultacji.

### 9. Grywalizacja & Portfel Voucherów
* `GET /api/vouchers` – Lista aktywnych voucherów i nagród.
* `POST /api/gamification/claim-voucher` – Wymiana punktów aktywności na voucher gminny.

### 10. Asystent AI & Doradca CUS
* `POST /api/assistant/chat` – Zapytanie do bazy wiedzy CUS (kryteria dochodowe 2026, procedury, godziny dyżurów).

---

## 🗄️ Baza Danych:
Baza danych tworzona jest automatycznie jako plik SQLite: `backend/smart_cus.db`.
Wszystkie tabele są wstępnie zainicjalizowane realistycznymi danymi dla Gminy Wieliszew.
