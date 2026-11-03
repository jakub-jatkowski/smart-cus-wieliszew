import os
from typing import Optional, List
from fastapi import FastAPI, HTTPException, Query
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
try:
    from . import database as db
except ImportError:
    try:
        import database as db
    except ImportError:
        from backend import database as db

# Initialize SQLite database on module load
db.init_db()

app = FastAPI(
    title="Smart CUS Wieliszew Backend API",
    version="2.0.0",
    description="Oficjalny serwer API Cyfrowego Hubu Usług Społecznych Gminy Wieliszew (GovTech 2.0)",
    docs_url="/docs",
    redoc_url="/redoc"
)

# Enable CORS for all origins (Android app, local web, emulator)
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# ----------------- Pydantic Models -----------------
class ProfileUpdateRequest(BaseModel):
    full_name: str
    village: str
    ice_contact: str

class NeedPinCreateRequest(BaseModel):
    title: str
    description: str
    village: str
    category: str
    urgency: str
    points: Optional[int] = 50

class TimeBankCreateRequest(BaseModel):
    user_name: str
    village: str
    type: str # 'OFFERING' or 'REQUESTING'
    category: str
    title: str
    description: str
    hours: float

class FoodDonationRequest(BaseModel):
    item_name: str
    category: str
    quantity: str
    expiry_date: str
    donor_name: Optional[str] = "Mieszkaniec Wieliszewa"

class AppointmentBookRequest(BaseModel):
    specialist_id: str
    resident_name: str
    date_time: str

class ClaimRewardRequest(BaseModel):
    title: str
    partner: str
    discount: str

class ChatQueryRequest(BaseModel):
    message: str

# ----------------- Root & Health -----------------
@app.get("/")
def read_root():
    return {
        "system": "Smart CUS Wieliszew Backend API",
        "version": "2.0.0",
        "status": "OPERATIONAL",
        "institution": "Centrum Usług Społecznych w Wieliszewie",
        "address": "ul. Modlińska 65, 05-135 Wieliszew",
        "documentation": "/docs"
    }

@app.get("/api/health")
def healthcheck():
    resident = db.get_resident()
    return {
        "status": "healthy",
        "database": "sqlite_connected",
        "active_resident": resident["full_name"] if resident else None,
        "api_version": "2.0.0"
    }

# ----------------- 1. Resident & WKM -----------------
@app.get("/api/resident")
def get_resident_profile():
    res = db.get_resident()
    if not res:
        raise HTTPException(status_code=404, detail="Resident not found")
    return res

@app.put("/api/resident")
def update_profile(req: ProfileUpdateRequest):
    res = db.update_resident(req.full_name, req.village, req.ice_contact)
    return {"message": "Profile updated successfully", "resident": res}

# ----------------- 2. Notifications -----------------
@app.get("/api/notifications")
def list_notifications():
    return db.get_notifications()

@app.post("/api/notifications/{notif_id}/read")
def read_notification(notif_id: str):
    db.mark_notification_read(notif_id)
    return {"status": "ok", "id": notif_id}

# ----------------- 3. Need Pins (Mikrowolontariat) -----------------
@app.get("/api/need-pins")
def list_pins(village: Optional[str] = None, category: Optional[str] = None):
    return db.get_need_pins(village=village, category=category)

@app.post("/api/need-pins")
def create_pin(req: NeedPinCreateRequest):
    pin_id = db.add_need_pin(
        title=req.title,
        description=req.description,
        village=req.village,
        category=req.category,
        urgency=req.urgency,
        points=req.points or 50
    )
    return {"status": "created", "pin_id": pin_id}

@app.post("/api/need-pins/{pin_id}/accept")
def accept_pin(pin_id: str):
    db.accept_need_pin(pin_id)
    return {"status": "accepted", "pin_id": pin_id, "awarded_points": 50}

@app.post("/api/need-pins/{pin_id}/complete")
def complete_pin(pin_id: str):
    db.complete_need_pin(pin_id)
    return {"status": "completed", "pin_id": pin_id}

# ----------------- 4. Time Bank (Bank Czasu) -----------------
@app.get("/api/time-bank")
def list_time_bank(type: Optional[str] = None):
    return db.get_time_bank(entry_type=type)

@app.post("/api/time-bank")
def create_time_bank_entry(req: TimeBankCreateRequest):
    entry_id = db.add_time_bank(
        user_name=req.user_name,
        village=req.village,
        entry_type=req.type,
        category=req.category,
        title=req.title,
        description=req.description,
        hours=req.hours
    )
    return {"status": "created", "entry_id": entry_id}

@app.post("/api/time-bank/{entry_id}/exchange")
def exchange_hours(entry_id: str):
    db.exchange_time_bank(entry_id)
    resident = db.get_resident()
    return {"status": "exchanged", "entry_id": entry_id, "new_balance": resident["time_balance"]}

# ----------------- 5. Equipment Rental -----------------
@app.get("/api/equipment")
def list_equipment():
    return db.get_equipment()

@app.post("/api/equipment/{eq_id}/reserve")
def reserve_equipment_item(eq_id: str):
    code = db.reserve_equipment(eq_id)
    return {"status": "reserved", "equipment_id": eq_id, "pickup_code": code}

# ----------------- 6. Food Sharing (Jadłodzielnia) -----------------
@app.get("/api/jadlodzielnia")
def list_food():
    return db.get_food_sharing()

@app.post("/api/jadlodzielnia/donate")
def donate_food(req: FoodDonationRequest):
    food_id = db.add_food_donation(
        item_name=req.item_name,
        category=req.category,
        quantity=req.quantity,
        expiry_date=req.expiry_date,
        donor_name=req.donor_name or "Mieszkaniec Wieliszewa"
    )
    return {"status": "donated", "food_id": food_id}

# ----------------- 7. Telehealth & Consultations -----------------
@app.get("/api/telehealth/specialists")
def list_specialists():
    return db.get_specialists()

@app.post("/api/telehealth/book")
def book_consultation(req: AppointmentBookRequest):
    app_id = db.book_appointment(req.specialist_id, req.resident_name, req.date_time)
    return {"status": "confirmed", "appointment_id": app_id}

# ----------------- 8. Gamification & Vouchers -----------------
@app.get("/api/vouchers")
def list_vouchers():
    return db.get_vouchers()

@app.post("/api/gamification/claim-voucher")
def claim_reward(req: ClaimRewardRequest):
    result = db.claim_voucher(req.title, req.partner, req.discount)
    resident = db.get_resident()
    return {"status": "claimed", "voucher": result, "remaining_points": resident["points"]}

# ----------------- 9. AI Assistant CUS Knowledge Base -----------------
@app.post("/api/assistant/chat")
def chat_with_assistant(req: ChatQueryRequest):
    query = req.message.lower()

    if any(w in query for w in ["kryter", "dochód", "świadczen", "zasił", "ile"]):
        reply = "W 2026 r. kryterium dochodowe w pomocy społecznej wynosi 1 010 zł dla osoby samotnie gospodarującej oraz 776 zł na osobę w rodzinie. CUS Wieliszew przyznaje zasiłki stałe (maksymalnie do 1 000 zł miesięcznie), okresowe oraz celowe."
        module_hint = "assistant"
    elif any(w in query for w in ["wózek", "balkonik", "sprzęt", "kul", "łóżko", "wypożycz"]):
        reply = "Gminna Wypożyczalnia CUS przy ul. Modlińskiej 65 dysponuje bezpłatnymi wózkami lekkimi, balkonikami 4-kołowymi oraz kulami ortopedycznymi. Sprzęt możesz zarezerwować bezpośrednio w aplikacji, otrzymując unikalny kod odbioru."
        module_hint = "sharing"
    elif any(w in query for w in ["jadłodziel", "jedzen", "lodówk", "dar", "żywnoś"]):
        reply = "Gminna Jadłodzielnia Wieliszew znajduje się przy ul. Modlińskiej 65 (obok budynku CUS) i działa całodobowo (24/7). Każdy mieszkaniec może bezpłatnie skorzystać z żywności lub przekazać własne nadwyżki żywieniowe z datą ważności."
        module_hint = "sharing"
    elif any(w in query for w in ["psycholog", "prawnik", "radca", "teleopiek", "wideo", "konsultac"]):
        reply = "W CUS Wieliszew bezpłatnych konsultacji udzielają: psycholog mgr Anna Wiśniewska oraz radca prawny mec. Piotr Zieliński. Możesz połączyć się wideo natychmiast lub zarezerwować dogodny termin w zakładce Teleopieka."
        module_hint = "telehealth"
    elif any(w in query for w in ["kart", "wkm", "zniżk", "voucher", "bilet", "basen"]):
        reply = "Wieliszewska Karta Mieszkańca (WKM) w aplikacji Smart CUS posiada zabezpieczenie kodem QR TOTP odświeżanym co 45s oraz rewers z danymi ratunkowymi ICE. Uprawnia do zniżek na obiekty OSiR, GCK oraz bezpłatne kajaki."
        module_hint = "card"
    elif any(w in query for w in ["bank czasu", "godzin", "wymian", "pomoc"]):
        reply = "Lokalny Bank Czasu w Wieliszewie działa na zasadzie '1 godzina za 1 godzinę'. Możesz zaoferować swoją umiejętność (np. naprawy, obsługa smartfona, opieka nad ogrodem) lub skorzystać z pomocy sąsiada."
        module_hint = "timebank"
    else:
        reply = "Centrum Usług Społecznych w Wieliszewie (ul. Modlińska 65, tel. 22 782 01 00) jest do Twojej dyspozycji w godzinach 08:00 - 16:00. W czym jeszcze mogę Ci dzisiaj pomóc?"
        module_hint = "assistant"

    return {
        "reply": reply,
        "module_hint": module_hint,
        "timestamp": "Teraz"
    }

