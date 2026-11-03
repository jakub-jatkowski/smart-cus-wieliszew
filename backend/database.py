import sqlite3
import os
import json
from datetime import datetime

DB_FILE = os.path.join(os.path.dirname(__file__), "smart_cus.db")

def get_connection():
    conn = sqlite3.connect(DB_FILE)
    conn.row_factory = sqlite3.Row
    return conn

def init_db():
    conn = get_connection()
    c = conn.cursor()

    # 1. Resident Profile & Card
    c.execute("""
    CREATE TABLE IF NOT EXISTS resident (
        id INTEGER PRIMARY KEY,
        full_name TEXT NOT NULL,
        village TEXT NOT NULL,
        ice_contact TEXT NOT NULL,
        card_number TEXT NOT NULL,
        dynamic_token TEXT NOT NULL,
        points INTEGER NOT NULL DEFAULT 420,
        time_balance REAL NOT NULL DEFAULT 5.5,
        senior_status BOOLEAN NOT NULL DEFAULT 0,
        valid_until TEXT NOT NULL
    );
    """)

    # 2. Need Pins (Mikrowolontariat)
    c.execute("""
    CREATE TABLE IF NOT EXISTS need_pins (
        id TEXT PRIMARY KEY,
        title TEXT NOT NULL,
        description TEXT NOT NULL,
        village TEXT NOT NULL,
        category TEXT NOT NULL,
        urgency TEXT NOT NULL,
        lat REAL NOT NULL,
        lng REAL NOT NULL,
        points INTEGER NOT NULL DEFAULT 50,
        status TEXT NOT NULL DEFAULT 'OPEN',
        accepted_by TEXT,
        created_at TEXT NOT NULL
    );
    """)

    # 3. Time Bank (Bank Czasu)
    c.execute("""
    CREATE TABLE IF NOT EXISTS time_bank (
        id TEXT PRIMARY KEY,
        user_name TEXT NOT NULL,
        village TEXT NOT NULL,
        type TEXT NOT NULL,
        category TEXT NOT NULL,
        title TEXT NOT NULL,
        description TEXT NOT NULL,
        hours REAL NOT NULL,
        status TEXT NOT NULL DEFAULT 'ACTIVE',
        created_at TEXT NOT NULL
    );
    """)

    # 4. Equipment (Wypożyczalnia sprzętu rehabilitacyjnego)
    c.execute("""
    CREATE TABLE IF NOT EXISTS equipment (
        id TEXT PRIMARY KEY,
        name TEXT NOT NULL,
        category TEXT NOT NULL,
        is_available BOOLEAN NOT NULL DEFAULT 1,
        location TEXT NOT NULL,
        pickup_code TEXT
    );
    """)

    # 5. Food Sharing (Gminna Jadłodzielnia)
    c.execute("""
    CREATE TABLE IF NOT EXISTS food_sharing (
        id TEXT PRIMARY KEY,
        item_name TEXT NOT NULL,
        category TEXT NOT NULL,
        quantity TEXT NOT NULL,
        expiry_date TEXT NOT NULL,
        donor_name TEXT NOT NULL,
        created_at TEXT NOT NULL
    );
    """)

    # 6. Telehealth Specialists & Appointments
    c.execute("""
    CREATE TABLE IF NOT EXISTS specialists (
        id TEXT PRIMARY KEY,
        name TEXT NOT NULL,
        type TEXT NOT NULL,
        schedule TEXT NOT NULL,
        is_available BOOLEAN NOT NULL DEFAULT 1
    );
    """)

    c.execute("""
    CREATE TABLE IF NOT EXISTS appointments (
        id TEXT PRIMARY KEY,
        specialist_id TEXT NOT NULL,
        resident_name TEXT NOT NULL,
        date_time TEXT NOT NULL,
        status TEXT NOT NULL DEFAULT 'CONFIRMED'
    );
    """)

    # 7. Municipal Notifications
    c.execute("""
    CREATE TABLE IF NOT EXISTS notifications (
        id TEXT PRIMARY KEY,
        title TEXT NOT NULL,
        description TEXT NOT NULL,
        is_alert BOOLEAN NOT NULL DEFAULT 0,
        time_ago TEXT NOT NULL,
        is_read BOOLEAN NOT NULL DEFAULT 0
    );
    """)

    # 8. Vouchers
    c.execute("""
    CREATE TABLE IF NOT EXISTS vouchers (
        id TEXT PRIMARY KEY,
        code TEXT NOT NULL,
        title TEXT NOT NULL,
        partner TEXT NOT NULL,
        discount TEXT NOT NULL,
        valid_until TEXT NOT NULL,
        is_redeemed BOOLEAN NOT NULL DEFAULT 0
    );
    """)

    # Seed initial data if table empty
    c.execute("SELECT COUNT(*) FROM resident")
    if c.fetchone()[0] == 0:
        seed_data(c)

    conn.commit()
    conn.close()

def seed_data(c):
    # Resident
    c.execute("""
    INSERT INTO resident (id, full_name, village, ice_contact, card_number, dynamic_token, points, time_balance, senior_status, valid_until)
    VALUES (1, 'Janina Kowalska', 'Wieliszew', '+48 601 234 567', 'WKM-2026-88492-X9', 'WKM-SECURE-992', 420, 5.5, 0, '12/2027');
    """)

    # Notifications
    c.execute("""
    INSERT INTO notifications (id, title, description, is_alert, time_ago, is_read) VALUES
    ('n1', 'Ostrzeżenie IMGW – Silny Wiatr', 'W rejonie Zalewu Zegrzyńskiego prognozowane są porywy wiatru do 75 km/h.', 1, '15 min temu', 0),
    ('n2', 'Harmonogram Warsztatów Cyfrowych', 'Gminne warsztaty ze smartfonów dla seniorów w Łajskach w każdy czwartek o 10:00.', 0, '2 godz. temu', 0),
    ('n3', 'Bezpłatny Transport CUS Door-to-Door', 'Zgłoszenia na przejazdy do lekarzy w Legionowie i Nowym Dworze przyjmujemy z 2-dniowym wyprzedzeniem.', 0, 'Wczoraj', 1);
    """)

    # Need Pins
    c.execute("""
    INSERT INTO need_pins (id, title, description, village, category, urgency, lat, lng, points, status, accepted_by, created_at) VALUES
    ('p1', 'Pomoc w zakupach leków dla p. Heleny', 'Potrzebna pomoc w wykupieniu recepty w aptece przy ul. Modlińskiej.', 'Wieliszew', 'SHOPPING', 'TODAY', 52.4485, 20.9785, 50, 'OPEN', NULL, '2026-11-03 08:30'),
    ('p2', 'Wyprowadzenie psa rano i wieczorem', 'Starszy pan po operacji kolana potrzebuje pomocy w wyprowadzeniu labradora.', 'Łajski', 'PET_CARE', 'THIS_WEEK', 52.4278, 20.9632, 25, 'OPEN', NULL, '2026-11-03 09:00'),
    ('p3', 'Prace porządkowe w ogrodzie seniora', 'Grabienie liści i przygotowanie rabat do zimy dla seniorki.', 'Skrzeszew', 'GARDEN', 'FLEXIBLE', 52.4412, 20.9389, 30, 'OPEN', NULL, '2026-11-02 14:15'),
    ('p4', 'Drobna pomoc przy konfiguracji telefonu', 'Pomoc w obsłudze aplikacji CUS Wieliszew i kontaktu z rodziną.', 'Michałów-Reginów', 'REPAIR', 'FLEXIBLE', 52.4190, 20.9950, 40, 'OPEN', NULL, '2026-11-03 07:45');
    """)

    # Time Bank
    c.execute("""
    INSERT INTO time_bank (id, user_name, village, type, category, title, description, hours, status, created_at) VALUES
    ('tb1', 'Tadeusz N.', 'Wieliszew', 'OFFERING', 'Naprawy domowe', 'Drobne naprawy hydrauliczne i stolarskie', 'Wymiana uszczelek, regulacja zamków w drzwiach i oknach.', 1.0, 'ACTIVE', '2026-11-02'),
    ('tb2', 'Magdalena K.', 'Michałów-Reginów', 'OFFERING', 'Edukacja cyfrowa', 'Cierpliwa nauka obsługi smartfona i e-Karty', 'Dla początkujących seniorów: SMS, zdjęcia, internet.', 1.5, 'ACTIVE', '2026-11-03'),
    ('tb3', 'Stanisław B.', 'Skrzeszew', 'REQUESTING', 'Ogród', 'Szukam pomocy przy rąbaniu drewna opałowego', 'Około 1-2 godzin pracy na działce.', 2.0, 'ACTIVE', '2026-11-01');
    """)

    # Equipment
    c.execute("""
    INSERT INTO equipment (id, name, category, is_available, location, pickup_code) VALUES
    ('eq1', 'Wózek inwalidzki aluminiowy lekki (Vermeiren)', 'Wózki', 1, 'Magazyn CUS, ul. Modlińska 65', NULL),
    ('eq2', 'Balkonik rehabilitacyjny 4-kołowy z koszykiem', 'Balkoniki', 1, 'Magazyn CUS, ul. Modlińska 65', NULL),
    ('eq3', 'Kule łokciowe z amortyzatorem (para)', 'Kule', 1, 'Filia CUS Łajski', NULL),
    ('eq4', 'Łóżko rehabilitacyjne sterowane pilotem', 'Łóżka', 0, 'CUS Wieliszew (Wypożyczone)', 'CUS-W-2026-449');
    """)

    # Food Sharing
    c.execute("""
    INSERT INTO food_sharing (id, item_name, category, quantity, expiry_date, donor_name, created_at) VALUES
    ('fs1', 'Chleb razowy wieloziarnisty (Piekarnia Wieliszew)', 'Pieczywo', '4 bochenki', '2026-11-05', 'Piekarnia Gminna', '2026-11-03 07:00'),
    ('fs2', 'Jabłka grójeckie prosto z sadu', 'Owoce', '8 kg', '2026-11-12', 'Sadownik Wieliszew', '2026-11-03 08:30'),
    ('fs3', 'Zupy jarzynowe pasteryzowane w słoikach', 'Dania gotowe', '6 słoików', '2026-11-20', 'Koło Gospodyń', '2026-11-02 16:00');
    """)

    # Specialists
    c.execute("""
    INSERT INTO specialists (id, name, type, schedule, is_available) VALUES
    ('sp1', 'mgr Anna Wiśniewska', 'Psycholog CUS', 'Dziś 09:00 - 15:00', 1),
    ('sp2', 'mec. Piotr Zieliński', 'Radca Prawny CUS', 'Wtorki i Czwartki 11:00 - 16:00', 1),
    ('sp3', 'mgr Ewa Nowak', 'Koordynator Usług Społecznych', 'Pon - Pt 08:00 - 16:00', 1);
    """)

    # Vouchers
    c.execute("""
    INSERT INTO vouchers (id, code, title, partner, discount, valid_until, is_redeemed) VALUES
    ('v1', 'KAI-ZEGRZE-2026', 'Zniżka 50% na wypożyczenie kajaków', 'Przystań Zalew Zegrzyński', '-50%', '31.12.2026', 0),
    ('v2', 'OSIR-BASEN-FREE', 'Bezpłatny wstęp na basen OSiR', 'OSiR Wieliszew', '100% Gratis', '30.11.2026', 0),
    ('v3', 'GCK-KINO-20', 'Bilet do Kina Społecznego GCK Łajski', 'Gminne Centrum Kultury', '-20 zł', '15.12.2026', 0);
    """)

# CRUD Helpers
def get_resident():
    conn = get_connection()
    c = conn.cursor()
    c.execute("SELECT * FROM resident WHERE id = 1")
    row = c.fetchone()
    conn.close()
    return dict(row) if row else None

def update_resident(full_name=None, village=None, ice_contact=None, points_delta=0, time_delta=0.0):
    conn = get_connection()
    c = conn.cursor()
    if full_name:
        c.execute("UPDATE resident SET full_name = ?, village = ?, ice_contact = ? WHERE id = 1", (full_name, village, ice_contact))
    if points_delta != 0:
        c.execute("UPDATE resident SET points = points + ? WHERE id = 1", (points_delta,))
    if time_delta != 0.0:
        c.execute("UPDATE resident SET time_balance = time_balance + ? WHERE id = 1", (time_delta,))
    conn.commit()
    conn.close()
    return get_resident()

def get_notifications():
    conn = get_connection()
    rows = conn.execute("SELECT * FROM notifications ORDER BY id ASC").fetchall()
    conn.close()
    return [dict(r) for r in rows]

def mark_notification_read(notif_id):
    conn = get_connection()
    conn.execute("UPDATE notifications SET is_read = 1 WHERE id = ?", (notif_id,))
    conn.commit()
    conn.close()

def get_need_pins(village=None, category=None):
    conn = get_connection()
    query = "SELECT * FROM need_pins WHERE 1=1"
    params = []
    if village and village != "Wszystkie":
        query += " AND LOWER(village) = LOWER(?)"
        params.append(village)
    if category:
        query += " AND category = ?"
        params.append(category)
    query += " ORDER BY created_at DESC"
    rows = conn.execute(query, params).fetchall()
    conn.close()
    return [dict(r) for r in rows]

def add_need_pin(title, description, village, category, urgency, points=50):
    pin_id = f"p-{datetime.now().strftime('%Y%m%d%H%M%S')}"
    conn = get_connection()
    conn.execute("""
    INSERT INTO need_pins (id, title, description, village, category, urgency, lat, lng, points, status, accepted_by, created_at)
    VALUES (?, ?, ?, ?, ?, ?, 52.4485, 20.9785, ?, 'OPEN', NULL, ?)
    """, (pin_id, title, description, village, category, urgency, points, datetime.now().strftime('%Y-%m-%d %H:%M')))
    conn.commit()
    conn.close()
    return pin_id

def accept_need_pin(pin_id, volunteer_name="Janina Kowalska"):
    conn = get_connection()
    conn.execute("UPDATE need_pins SET status = 'IN_PROGRESS', accepted_by = ? WHERE id = ?", (volunteer_name, pin_id))
    # Award gamification points
    conn.execute("UPDATE resident SET points = points + 50 WHERE id = 1")
    conn.commit()
    conn.close()
    return True

def complete_need_pin(pin_id):
    conn = get_connection()
    conn.execute("UPDATE need_pins SET status = 'COMPLETED' WHERE id = ?", (pin_id,))
    conn.commit()
    conn.close()
    return True

def get_time_bank(entry_type=None):
    conn = get_connection()
    if entry_type:
        rows = conn.execute("SELECT * FROM time_bank WHERE type = ? ORDER BY created_at DESC", (entry_type,)).fetchall()
    else:
        rows = conn.execute("SELECT * FROM time_bank ORDER BY created_at DESC").fetchall()
    conn.close()
    return [dict(r) for r in rows]

def add_time_bank(user_name, village, entry_type, category, title, description, hours):
    entry_id = f"tb-{datetime.now().strftime('%Y%m%d%H%M%S')}"
    conn = get_connection()
    conn.execute("""
    INSERT INTO time_bank (id, user_name, village, type, category, title, description, hours, status, created_at)
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'ACTIVE', ?)
    """, (entry_id, user_name, village, entry_type, category, title, description, hours, datetime.now().strftime('%Y-%m-%d')))
    conn.commit()
    conn.close()
    return entry_id

def exchange_time_bank(entry_id, user_id=1):
    conn = get_connection()
    c = conn.cursor()
    c.execute("SELECT * FROM time_bank WHERE id = ?", (entry_id,))
    row = c.fetchone()
    if row:
        hours = row['hours']
        entry_type = row['type']
        # If exchanging for an offer, subtract hours; if fulfilling request, add hours
        delta = -hours if entry_type == 'OFFERING' else hours
        c.execute("UPDATE resident SET time_balance = time_balance + ? WHERE id = 1", (delta,))
        conn.commit()
    conn.close()
    return True

def get_equipment():
    conn = get_connection()
    rows = conn.execute("SELECT * FROM equipment ORDER BY id ASC").fetchall()
    conn.close()
    return [dict(r) for r in rows]

def reserve_equipment(eq_id):
    pickup_code = f"CUS-W-2026-{datetime.now().strftime('%f')[:4]}"
    conn = get_connection()
    conn.execute("UPDATE equipment SET is_available = 0, pickup_code = ? WHERE id = ?", (pickup_code, eq_id))
    conn.commit()
    conn.close()
    return pickup_code

def get_food_sharing():
    conn = get_connection()
    rows = conn.execute("SELECT * FROM food_sharing ORDER BY created_at DESC").fetchall()
    conn.close()
    return [dict(r) for r in rows]

def add_food_donation(item_name, category, quantity, expiry_date, donor_name="Mieszkaniec Wieliszewa"):
    food_id = f"fs-{datetime.now().strftime('%Y%m%d%H%M%S')}"
    conn = get_connection()
    conn.execute("""
    INSERT INTO food_sharing (id, item_name, category, quantity, expiry_date, donor_name, created_at)
    VALUES (?, ?, ?, ?, ?, ?, ?)
    """, (food_id, item_name, category, quantity, expiry_date, donor_name, datetime.now().strftime('%Y-%m-%d %H:%M')))
    conn.commit()
    conn.close()
    return food_id

def get_specialists():
    conn = get_connection()
    rows = conn.execute("SELECT * FROM specialists ORDER BY id ASC").fetchall()
    conn.close()
    return [dict(r) for r in rows]

def book_appointment(specialist_id, resident_name, date_time):
    app_id = f"apt-{datetime.now().strftime('%Y%m%d%H%M%S')}"
    conn = get_connection()
    conn.execute("""
    INSERT INTO appointments (id, specialist_id, resident_name, date_time, status)
    VALUES (?, ?, ?, ?, 'CONFIRMED')
    """, (app_id, specialist_id, resident_name, date_time))
    conn.commit()
    conn.close()
    return app_id

def get_vouchers():
    conn = get_connection()
    rows = conn.execute("SELECT * FROM vouchers ORDER BY id ASC").fetchall()
    conn.close()
    return [dict(r) for r in rows]

def claim_voucher(title, partner, discount):
    voucher_id = f"v-{datetime.now().strftime('%Y%m%d%H%M%S')}"
    code = f"WKM-{partner[:3].upper()}-{datetime.now().strftime('%f')[:4]}"
    conn = get_connection()
    conn.execute("""
    INSERT INTO vouchers (id, code, title, partner, discount, valid_until, is_redeemed)
    VALUES (?, ?, ?, ?, ?, '31.12.2026', 0)
    """, (voucher_id, code, title, partner, discount))
    # Deduct 100 points
    conn.execute("UPDATE resident SET points = MAX(0, points - 100) WHERE id = 1")
    conn.commit()
    conn.close()
    return {"id": voucher_id, "code": code}

