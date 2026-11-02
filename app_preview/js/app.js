/**
 * Smart CUS Wieliszew - Production-Ready Multi-Module Web & PWA Engine
 * Complete Social Services Hub of Gmina Wieliszew
 */

// Default Initial State
const DEFAULT_STATE = {
  user: {
    name: "Janina Kowalska",
    village: "Wieliszew",
    cardNum: "WKM-2026-88492",
    iceContact: "Córka Anna: +48 601 234 567",
    timeHours: 5.5,
    points: 420,
    seniorMode: false,
    fontScale: "font-scale-md"
  },
  totpSeconds: 45,
  vouchers: [],
  registeredEvents: ["ev-1"],
  timeBankHistory: [
    { id: "h-1", date: "Wczoraj, 14:20", text: "Zgłoszenie oferty: Nauka obsługi smartfona", change: "+0h", balance: "5.5h" },
    { id: "h-2", date: "28.10.2026", text: "Pomoc w podwiezieniu do lekarza w Legionowie", change: "+1.5h", balance: "5.5h" },
    { id: "h-3", date: "24.10.2026", text: "Skorzystanie z drobnej naprawy uszczelki", change: "-1.0h", balance: "4.0h" }
  ],
  notifications: [
    { id: "n-1", type: "alert", title: "Ostrzeżenie IMGW: Silny wiatr nad Zalewem Zegrzyńskim", time: "10 min temu", read: false },
    { id: "n-2", type: "info", title: "Ruszyły zapisy na bezpłatną rehabilitację domową CUS", time: "Dziś, 09:30", read: false }
  ],
  events: [
    {
      id: "ev-1",
      title: "Gimnastyka \"Zdrowy Kręgosłup 60+\"",
      category: "Zdrowie",
      badge: "Profilaktyka Zdrowia",
      dateText: "Czwartki, godz. 10:00",
      location: "Hala Sportowa Wieliszew, ul. Modlińska 65",
      instructor: "mgr Paweł Kamiński (Fizjoterapeuta CUS)",
      desc: "Zajęcia usprawniające dla seniorów z elementami stretchingu i ćwiczeniami oddechowymi. Bezpłatne z e-Kartą.",
      spotsTotal: 20,
      spotsTaken: 13
    },
    {
      id: "ev-2",
      title: "Smartfon i e-Usługi bez tajemnic dla seniora",
      category: "Cyfrowe",
      badge: "Edukacja Cyfrowa",
      dateText: "12 Listopada 2026, godz. 11:30",
      location: "Gminna Biblioteka Publiczna w Skrzeszewie",
      instructor: "mgr Tomasz Wiśniewski (Koordynator ds. Cyfryzacji)",
      desc: "Praktyczna obsługa e-Karty Mieszkańca, wideorozmów z lekarzem, e-recepty i bezpieczeństwa przed oszustami.",
      spotsTotal: 15,
      spotsTaken: 8
    },
    {
      id: "ev-3",
      title: "Kawiarenka Międzypokoleniowa & Brydż",
      category: "Kultura",
      badge: "Integracja",
      dateText: "Piątki, godz. 16:00",
      location: "Gminne Centrum Kultury w Łajskach",
      instructor: "Zespół Wolontariatu CUS",
      desc: "Spotkanie przy kawie i herbacie, wymiana doświadczeń międzypokoleniowych, gry planszowe i brydż.",
      spotsTotal: 25,
      spotsTaken: 19
    }
  ],
  mapPins: [
    {
      id: "pin-1",
      title: "Wniesienie zgrzewki wody i leków z apteki",
      category: "Zakupy",
      icon: "🛒",
      urgency: "Pilne (dzisiaj)",
      village: "Wieliszew",
      address: "Rejon ul. Kościelnej / Park Świętojański",
      desc: "Pani Maria (82 l.) po zabiegu kolana potrzebuje pomocy w odbiorze leków z apteki i wniesieniu wody na 1. piętro.",
      points: 50,
      lat: 52.4572,
      lng: 20.9785,
      status: "OPEN" // OPEN, IN_PROGRESS, COMPLETED
    },
    {
      id: "pin-2",
      title: "Podwiezienie do przychodni w Legionowie",
      category: "Transport",
      icon: "🚗",
      urgency: "W tym tygodniu",
      village: "Skrzeszew",
      address: "Rejon ul. Polnej i Partyzantów",
      desc: "Pan Stanisław potrzebuje transportu na badanie USG we wtorek o 9:30 z powrotem do Skrzeszewa. Porusza się o lasce.",
      points: 80,
      lat: 52.4630,
      lng: 20.9250,
      status: "OPEN"
    },
    {
      id: "pin-3",
      title: "Drobna pomoc: konfiguracja nowego dekodera TV",
      category: "Drobna naprawa",
      icon: "🔧",
      urgency: "Elastyczny termin",
      village: "Michałów-Reginów",
      address: "Okolice stacji PKP Michałów-Reginów",
      desc: "Podłączenie kabla HDMI i ponowne przeskanowanie programów cyfrowych dla małżeństwa seniorów.",
      points: 45,
      lat: 52.4330,
      lng: 20.9820,
      status: "OPEN"
    },
    {
      id: "pin-4",
      title: "Wyprowadzenie psa po południu",
      category: "Opieka",
      icon: "🐕",
      urgency: "Pilne (dzisiaj)",
      village: "Łajski",
      address: "Rejon ul. Sikorskiego",
      desc: "Krótki 30-minutowy spacer z łagodnym psem. Właścicielka leży z grypą i potrzebuje wsparcia sąsiedzkiego.",
      points: 35,
      lat: 52.4280,
      lng: 20.9500,
      status: "OPEN"
    }
  ],
  timeBank: [
    {
      id: "tb-1",
      title: "Oferuję: Naprawy hydrauliczne i złota rączka",
      author: "Tadeusz N. (Wieliszew)",
      type: "OFFERING",
      hours: 1.0,
      desc: "Wymiana uszczelek, naprawa kranu, regulacja okien. Chętnie pomogę w zamian za 1h w Banku Czasu.",
      rating: "⭐ 5.0 (12 opinii)"
    },
    {
      id: "tb-2",
      title: "Oferuję: Nauka obsługi smartfona i komputera",
      author: "Magdalena K. (Michałów-Reginów)",
      type: "OFFERING",
      hours: 1.0,
      desc: "Spokojna, cierpliwa nauka pisania wiadomości, wideorozmów z wnukami i logowania do e-Karty.",
      rating: "⭐ 4.9 (8 opinii)"
    },
    {
      id: "tb-3",
      title: "Poszukuję: Pomoc w zgrabieniu liści w ogrodzie",
      author: "Helena W. (Skrzeszew)",
      type: "REQUESTING",
      hours: 2.0,
      desc: "Niewielki ogródek. Zapewniam grabie, worki, ciepłą herbatę i domowe ciasto drożdżowe!",
      rating: "⭐ 4.8 (5 opinii)"
    }
  ],
  badges: [
    { id: "b-1", title: "Serce Wieliszewa", emoji: "❤️", unlocked: true, desc: "Przyznana za wykonanie co najmniej 3 akcji wolontariatu na rzecz seniorów." },
    { id: "b-2", title: "Aktywny Sąsiad", emoji: "🤝", unlocked: true, desc: "Przyznana za pierwszą wymianę umiejętności w Banku Czasu." },
    { id: "b-3", title: "Mistrz Ekologii", emoji: "🌱", unlocked: true, desc: "Przyznana za udział w Jadłodzielni Wieliszew i niemarnowanie żywności." },
    { id: "b-4", title: "Promotor Zdrowia", emoji: "🏅", unlocked: false, desc: "Zapisz się i weź udział w 3 warsztatach prozdrowotnych CUS." },
    { id: "b-5", title: "Cyfrowy Senior", emoji: "📱", unlocked: false, desc: "Skorzystaj z wirtualnego asystenta głosowego oraz e-Karty." },
    { id: "b-6", title: "Lider Społeczności", emoji: "⭐", unlocked: false, desc: "Osiągnij 600 punktów aktywności w Gminie Wieliszew." }
  ],
  rewards: [
    {
      id: "rew-1",
      title: "Karnet 1h: Kajak lub Rower Wodny nad Narwią",
      category: "Rekreacja i Sport",
      partner: "OSiR & Przystań Wieliszew",
      cost: 250,
      desc: "Bezpłatne wypożyczenie kajaka lub rowerka wodnego na 1 godzinę w sezonie letnim."
    },
    {
      id: "rew-2",
      title: "Zniżka 30% na bilet na spektakl / koncert",
      category: "Kultura",
      partner: "Gminne Centrum Kultury w Łajskach",
      cost: 150,
      desc: "Zniżka obowiązuje na dowolne wydarzenie biletowane z repertuaru GCK."
    },
    {
      id: "rew-3",
      title: "Darmowa kawa sąsiedzka i ciasto domowe",
      category: "Gastronomia",
      partner: "Kawiarnia Przystań w Wieliszewie",
      cost: 100,
      desc: "Kawa z ekspresu (lub herbata) oraz świeże ciasto dnia w miłej atmosferze."
    },
    {
      id: "rew-4",
      title: "Karnet na basen OSiR (1 wejście 70 min)",
      category: "Sport i Rekreacja",
      partner: "Basen Wieliszew",
      cost: 200,
      desc: "Pojedynczy bilet wstępu na basen kryty w Wieliszewie ze strefą relaksu."
    }
  ],
  specialists: [
    {
      id: "sp-1",
      name: "mgr Anna Wiśniewska",
      role: "Psycholog CUS Wieliszew",
      today: true,
      slot: "Dzisiaj, godz. 13:30",
      desc: "Wsparcie emocjonalne dla seniorów i rodzin, pomoc w kryzysie, przeciwdziałanie samotności."
    },
    {
      id: "sp-2",
      name: "mec. Piotr Dąbrowski",
      role: "Radca Prawny / Pomoc Prawna",
      today: true,
      slot: "Dzisiaj, godz. 15:00",
      desc: "Nieodpłatna pomoc prawna: sprawy majątkowe, spadkowe, testamenty, dodatki mieszkaniowe."
    },
    {
      id: "sp-3",
      name: "mgr Elżbieta Zielińska",
      role: "Koordynator Indywidualnych Usług CUS",
      today: false,
      slot: "Jutro, godz. 09:00",
      desc: "Opieka wytchnieniowa, asystent osobisty osoby z niepełnosprawnością, plany wsparcia."
    }
  ],
  sharingItems: [
    {
      id: "sh-1",
      title: "Wózek inwalidzki lekki aluminiowy (składany)",
      category: "Sprzęt Rehabilitacyjny",
      location: "Punkt CUS Wieliszew, ul. Modlińska 65",
      status: "Dostępny od ręki (2 szt.)",
      desc: "Atestowany, po dezynfekcji. Bezpłatne wypożyczenie dla mieszkańców na okres do 6 miesięcy.",
      reserved: false,
      reserveCode: null
    },
    {
      id: "sh-2",
      title: "Balkonik czterokołowy z koszykiem na zakupy",
      category: "Sprzęt Rehabilitacyjny",
      location: "Świetlica Wiejska w Skrzeszewie",
      status: "Dostępny od ręki (1 szt.)",
      desc: "Lekka konstrukcja, blokada hamulców, wygodne siedzisko do odpoczynku.",
      reserved: false,
      reserveCode: null
    },
    {
      id: "sh-3",
      title: "Świeże jabłka i przetwory domowe (Jadłodzielnia)",
      category: "Jadłodzielnia Wieliszew",
      location: "Lodówka Jadłodzielni przy CUS (czynna 24/7)",
      status: "Dostępne (8 kg)",
      desc: "Dar od lokalnego gospodarstwa z Kałuszyna. Zachęcamy do bezpłatnego częstowania się!",
      reserved: false,
      reserveCode: null
    },
    {
      id: "sh-4",
      title: "Kule łokciowe z regulacją wysokości (para)",
      category: "Sprzęt Rehabilitacyjny",
      location: "Punkt CUS Wieliszew",
      status: "Dostępne (3 pary)",
      desc: "Ergonomiczny uchwyt z odblaskami dla bezpieczeństwa o zmroku.",
      reserved: false,
      reserveCode: null
    }
  ],
  chatHistory: [
    {
      sender: "assistant",
      text: "Dzień dobry Pani Janino! Jestem wirtualnym asystentem Smart CUS Wieliszew. W czym mogę dziś pomóc? Mogę sprawdzić Pani e-Kartę, zapisać na gimnastykę dla seniorów lub połączyć z psychologiem.",
      recommendations: [
        { title: "Zajęcia gimnastyczne Zdrowy Kręgosłup 60+", sub: "Czwartki, Hala Wieliszew", tab: "pane-card" },
        { title: "Twoja e-Karta Mieszkańca", sub: "Sprawdź kod QR i ulgi gminne", tab: "pane-card" }
      ]
    }
  ]
};

// Application State Object
let state = loadState();

// Global Variables
let leafletMap = null;
let leafletMarkers = [];
let callInterval = null;
let callDuration = 0;
let localCameraStream = null;

// ==========================================================================
// PERSISTENT STORAGE
// ==========================================================================
function loadState() {
  try {
    const raw = localStorage.getItem("smart_cus_state_v2");
    if (raw) {
      const parsed = JSON.parse(raw);
      // Merge with default state structure
      return Object.assign({}, DEFAULT_STATE, parsed, {
        user: Object.assign({}, DEFAULT_STATE.user, parsed.user || {})
      });
    }
  } catch (e) {
    console.error("Błąd odczytu localStorage:", e);
  }
  return JSON.parse(JSON.stringify(DEFAULT_STATE));
}

function saveState() {
  try {
    localStorage.setItem("smart_cus_state_v2", JSON.stringify(state));
  } catch (e) {
    console.error("Błąd zapisu localStorage:", e);
  }
}

function resetAppData() {
  if (confirm("Czy na pewno chcesz zresetować dane symulatora do stanu początkowego?")) {
    localStorage.removeItem("smart_cus_state_v2");
    state = JSON.parse(JSON.stringify(DEFAULT_STATE));
    saveState();
    location.reload();
  }
}

// ==========================================================================
// INITIALIZATION
// ==========================================================================
document.addEventListener("DOMContentLoaded", () => {
  initClock();
  initTotpTimer();
  applyUserPreferences();
  renderChatMessages();
  renderEvents();
  renderVouchers();
  renderNeedPins();
  renderTimeBank();
  renderGamification();
  renderSpecialists();
  renderSharingItems();
  renderBarcodeSvg();
  initPwaInstall();
  updateNotificationBadge();
});

// Apply theme, font scale, user name
function applyUserPreferences() {
  document.body.classList.toggle("senior-mode", state.user.seniorMode);
  
  document.body.classList.remove("font-scale-sm", "font-scale-md", "font-scale-lg", "font-scale-xl");
  document.body.classList.add(state.user.fontScale || "font-scale-md");

  // User elements
  const nameEl = document.getElementById("card-holder-name");
  if (nameEl) nameEl.textContent = state.user.name;

  const villageEl = document.getElementById("card-num-village");
  if (villageEl) villageEl.textContent = `${state.user.cardNum} • Sołectwo ${state.user.village}`;

  const iceEl = document.getElementById("card-ice-display");
  if (iceEl) iceEl.textContent = state.user.iceContact;

  const avatarEl = document.getElementById("user-avatar-initials");
  if (avatarEl) {
    const initials = state.user.name.split(" ").map(w => w[0]).join("").toUpperCase().slice(0, 2);
    avatarEl.textContent = initials || "WKM";
  }

  // Senior mode pill in header
  const seniorPill = document.getElementById("btn-toggle-senior");
  if (seniorPill) {
    seniorPill.classList.toggle("active", state.user.seniorMode);
    seniorPill.innerHTML = state.user.seniorMode
      ? "<span>👁️</span> Tryb Seniora: WŁĄCZONY"
      : "<span>👁️</span> Tryb Seniora (WCAG 2.1 AA)";
  }
}

// ==========================================================================
// PWA SERVICE WORKER
// ==========================================================================
let deferredPrompt = null;
function initPwaInstall() {
  if ("serviceWorker" in navigator) {
    navigator.serviceWorker.register("./sw.js").catch(err => console.log("SW registration error:", err));
  }
  window.addEventListener("beforeinstallprompt", (e) => {
    e.preventDefault();
    deferredPrompt = e;
    showToast("📱 Zainstaluj Smart CUS na telefonie jednym kliknięciem!");
  });
}

// Clock in Status Bar
function initClock() {
  const updateClock = () => {
    const now = new Date();
    const hh = String(now.getHours()).padStart(2, "0");
    const mm = String(now.getMinutes()).padStart(2, "0");
    const el = document.getElementById("android-clock");
    if (el) el.textContent = `${hh}:${mm}`;
  };
  updateClock();
  setInterval(updateClock, 10000);
}

// ==========================================================================
// TOTP DYNAMIC QR CODE & BARCODE ENGINE
// ==========================================================================
function initTotpTimer() {
  const bar = document.getElementById("totp-progress-bar");
  const sec = document.getElementById("totp-seconds");
  const secModal = document.getElementById("totp-seconds-modal");

  updateDynamicQrSvg();

  setInterval(() => {
    state.totpSeconds--;
    if (state.totpSeconds <= 0) {
      state.totpSeconds = 45;
      updateDynamicQrSvg();
    }
    if (sec) sec.textContent = state.totpSeconds;
    if (secModal) secModal.textContent = state.totpSeconds;
    if (bar) bar.style.width = `${(state.totpSeconds / 45) * 100}%`;
  }, 1000);
}

function updateDynamicQrSvg() {
  const svgMain = document.getElementById("qr-code-svg");
  const svgModal = document.getElementById("qr-code-fullscreen-svg");
  const qrContent = generateQrCodePattern();

  if (svgMain) svgMain.innerHTML = qrContent;
  if (svgModal) svgModal.innerHTML = qrContent;
}

function generateQrCodePattern() {
  const seed = Math.floor(Date.now() / 45000);
  const hashVal = Math.sin(seed) * 10000;
  const randOffset1 = Math.abs(Math.floor(hashVal % 60));
  const randOffset2 = Math.abs(Math.floor((hashVal * 2) % 60));

  return `
    <rect width="160" height="160" fill="#ffffff" rx="12"/>
    <!-- Corner Top-Left -->
    <rect x="12" y="12" width="38" height="38" fill="#137547" rx="6"/>
    <rect x="20" y="20" width="22" height="22" fill="#ffffff" rx="4"/>
    <rect x="25" y="25" width="12" height="12" fill="#137547" rx="2"/>
    <!-- Corner Top-Right -->
    <rect x="110" y="12" width="38" height="38" fill="#137547" rx="6"/>
    <rect x="118" y="20" width="22" height="22" fill="#ffffff" rx="4"/>
    <rect x="123" y="25" width="12" height="12" fill="#137547" rx="2"/>
    <!-- Corner Bottom-Left -->
    <rect x="12" y="110" width="38" height="38" fill="#137547" rx="6"/>
    <rect x="20" y="118" width="22" height="22" fill="#ffffff" rx="4"/>
    <rect x="25" y="123" width="12" height="12" fill="#137547" rx="2"/>
    <!-- Central Security Emblem -->
    <circle cx="80" cy="80" r="16" fill="#dda72a"/>
    <text x="80" y="84" text-anchor="middle" font-size="9" font-weight="900" fill="#000000">WKM</text>
    <!-- Dynamic Matrix Hash Blocks -->
    <rect x="${58 + (randOffset1 % 20)}" y="20" width="8" height="8" fill="#141c18"/>
    <rect x="${80 - (randOffset2 % 15)}" y="32" width="8" height="8" fill="#141c18"/>
    <rect x="62" y="${50 + (randOffset1 % 10)}" width="8" height="8" fill="#141c18"/>
    <rect x="90" y="60" width="8" height="8" fill="#141c18"/>
    <rect x="120" y="${68 + (randOffset2 % 12)}" width="8" height="8" fill="#141c18"/>
    <rect x="${65 + (randOffset2 % 18)}" y="100" width="8" height="8" fill="#141c18"/>
    <rect x="100" y="112" width="8" height="8" fill="#141c18"/>
    <rect x="125" y="128" width="8" height="8" fill="#141c18"/>
    <rect x="75" y="130" width="8" height="8" fill="#141c18"/>
  `;
}

function renderBarcodeSvg() {
  const svg = document.getElementById("card-barcode-svg");
  if (!svg) return;
  let lines = "";
  let x = 6;
  const pattern = [2, 1, 3, 1, 1, 2, 3, 2, 1, 1, 2, 3, 1, 2, 1, 3, 2, 1, 1, 3, 2, 2, 1, 1, 3];
  pattern.forEach(w => {
    lines += `<rect x="${x}" y="2" width="${w * 1.5}" height="24" fill="#000000"/>`;
    x += w * 1.5 + 2.5;
  });
  svg.innerHTML = lines;
}

// 3D Card Flip
function flipWkmCard() {
  const card = document.getElementById("wkm-card-wallet");
  if (card) {
    card.classList.toggle("flipped");
    playTone(980, 0.08);
    if ("vibrate" in navigator) navigator.vibrate(30);
  }
}

// Fullscreen QR Modal
function openFullscreenQrModal() {
  document.getElementById("modal-qr-fullscreen").classList.add("active");
  playTone(800, 0.1);
}

// ==========================================================================
// TAB SWITCHING & SUB-TABS
// ==========================================================================
function switchTab(paneId, btnElement) {
  document.querySelectorAll(".tab-pane").forEach(p => p.classList.remove("active"));
  const target = document.getElementById(paneId);
  if (target) target.classList.add("active");

  document.querySelectorAll(".nav-item").forEach(b => b.classList.remove("active"));
  if (btnElement) {
    btnElement.classList.add("active");
  } else {
    // Find matching bottom nav button
    const map = {
      "pane-assistant": "tab-btn-assistant",
      "pane-card": "tab-btn-card",
      "pane-map": "tab-btn-map",
      "pane-timebank": "tab-btn-timebank",
      "pane-gamification": "tab-btn-gamification",
      "pane-telehealth": "tab-btn-telehealth",
      "pane-sharing": "tab-btn-sharing"
    };
    const bId = map[paneId];
    if (bId) {
      const b = document.getElementById(bId);
      if (b) b.classList.add("active");
    }
  }

  // Scroll to top of app content
  const content = document.getElementById("app-content");
  if (content) content.scrollTop = 0;

  // Initialize Map if opened
  if (paneId === "pane-map") {
    setTimeout(initLeafletMap, 150);
  }
}

function switchCardSubtab(view) {
  const btnCard = document.getElementById("subtab-btn-card");
  const btnVouchers = document.getElementById("subtab-btn-vouchers");
  const viewCard = document.getElementById("card-subtab-main");
  const viewVouchers = document.getElementById("card-subtab-vouchers");

  if (view === "card-view") {
    btnCard.classList.add("active");
    btnVouchers.classList.remove("active");
    viewCard.style.display = "block";
    viewVouchers.style.display = "none";
  } else {
    btnCard.classList.remove("active");
    btnVouchers.classList.add("active");
    viewCard.style.display = "none";
    viewVouchers.style.display = "block";
    renderVouchers();
  }
}

// ==========================================================================
// SENIOR MODE & ACCESSIBILITY CONTROLS
// ==========================================================================
function toggleSeniorMode() {
  state.user.seniorMode = !state.user.seniorMode;
  saveState();
  applyUserPreferences();

  showToast(
    state.user.seniorMode
      ? "👁️ Włączono Tryb Seniora: powiększone czcionki, wysoki kontrast WCAG 2.1 AA"
      : "Przywrócono standardowy tryb wyświetlania"
  );
  playTone(state.user.seniorMode ? 659 : 440, 0.15);
}

function setFontScale(scaleClass) {
  state.user.fontScale = scaleClass;
  saveState();
  applyUserPreferences();
  showToast(`Zmieniono rozmiar tekstu aplikacji`);
}

// Desktop Simulator Viewport Switcher
let currentViewportMode = 0; // 0: 412px, 1: 580px, 2: fullscreen
function toggleViewportSize() {
  const frame = document.getElementById("phone-frame");
  const btn = document.getElementById("btn-toggle-viewport");
  currentViewportMode = (currentViewportMode + 1) % 3;

  frame.classList.remove("view-expanded", "view-fullscreen");
  if (currentViewportMode === 1) {
    frame.classList.add("view-expanded");
    if (btn) btn.innerHTML = "<span>📱</span> Widok: Tablet (580px)";
  } else if (currentViewportMode === 2) {
    frame.classList.add("view-fullscreen");
    if (btn) btn.innerHTML = "<span>📱</span> Widok: Pełny Ekran (100%)";
  } else {
    if (btn) btn.innerHTML = "<span>📱</span> Widok: Smartfon (412px)";
  }
  if (leafletMap) setTimeout(() => leafletMap.invalidateSize(), 300);
}

// Toast Notification
function showToast(msg) {
  const t = document.getElementById("toast-msg");
  if (t) {
    t.textContent = msg;
    t.style.display = "block";
    clearTimeout(t._timer);
    t._timer = setTimeout(() => {
      t.style.display = "none";
    }, 3600);
  }
}

// Audio synthesizer for feedback
function playTone(freq, dur) {
  try {
    const ctx = new (window.AudioContext || window.webkitAudioContext)();
    const osc = ctx.createOscillator();
    const gain = ctx.createGain();
    osc.frequency.value = freq;
    osc.type = "sine";
    gain.gain.setValueAtTime(0.12, ctx.currentTime);
    gain.gain.exponentialRampToValueAtTime(0.001, ctx.currentTime + dur);
    osc.connect(gain);
    gain.connect(ctx.destination);
    osc.start();
    osc.stop(ctx.currentTime + dur);
  } catch (e) {
    // audio context blocked or unsupported
  }
}

function simulateNfcTap() {
  playTone(880, 0.12);
  setTimeout(() => playTone(1320, 0.18), 130);
  if ("vibrate" in navigator) navigator.vibrate([80, 40, 80]);
  showToast("📶 Sukces! e-Karta zweryfikowana przez NFC (Czytnik w Hali Wieliszew)");
}

// ==========================================================================
// SOS EMERGENCY MODULE
// ==========================================================================
function openSosModal() {
  document.getElementById("modal-sos").classList.add("active");
  playTone(880, 0.25);
  setTimeout(() => playTone(587, 0.3), 260);
  if ("vibrate" in navigator) navigator.vibrate([200, 100, 200]);

  // Request actual GPS if available
  if ("geolocation" in navigator) {
    navigator.geolocation.getCurrentPosition(
      pos => {
        const gpsEl = document.getElementById("sos-gps-location");
        if (gpsEl) {
          gpsEl.textContent = `Współrzędne: ${pos.coords.latitude.toFixed(4)}° N, ${pos.coords.longitude.toFixed(4)}° E (Dokładność: ±${Math.round(pos.coords.accuracy)}m)`;
        }
      },
      err => {
        console.log("GPS denied or unavailable:", err.message);
      },
      { timeout: 5000 }
    );
  }
}

function logSosAction(type) {
  showToast(`🚨 Rozpoczynanie procedury alarmowej (${type}). Zgłoszenie zarejestrowano w CUS.`);
  closeModals();
}

function sendIceEmergencySms() {
  const gps = document.getElementById("sos-gps-location").textContent;
  const msg = encodeURIComponent(`[PILNY ALERT SOS SMART CUS] Potrzebuję pomocy! Mieszkaniec: ${state.user.name}. Moja lokalizacja: ${gps}`);
  const phone = "601234567";
  window.open(`sms:${phone}?body=${msg}`, "_blank");
  showToast("Przygotowano wiadomość SMS do kontaktu ICE z pozycją GPS!");
  closeModals();
}

// ==========================================================================
// NOTIFICATIONS & PROFILE
// ==========================================================================
function openNotificationsModal() {
  document.getElementById("modal-notifications").classList.add("active");
  // Mark all as read
  state.notifications.forEach(n => n.read = true);
  saveState();
  updateNotificationBadge();
}

function updateNotificationBadge() {
  const unreadCount = state.notifications.filter(n => !n.read).length;
  const badge = document.getElementById("notif-count");
  if (badge) {
    badge.textContent = unreadCount;
    badge.style.display = unreadCount > 0 ? "flex" : "none";
  }
}

function openProfileModal() {
  document.getElementById("profile-name-input").value = state.user.name;
  document.getElementById("profile-village-select").value = state.user.village;
  document.getElementById("profile-ice-input").value = state.user.iceContact;
  document.getElementById("modal-profile").classList.add("active");
}

function saveUserProfile() {
  const name = document.getElementById("profile-name-input").value.trim();
  const village = document.getElementById("profile-village-select").value;
  const ice = document.getElementById("profile-ice-input").value.trim();

  if (!name) {
    alert("Proszę wpisać imię i nazwisko.");
    return;
  }

  state.user.name = name;
  state.user.village = village;
  state.user.iceContact = ice;
  saveState();
  applyUserPreferences();
  closeModals();
  showToast("Zaktualizowano profil mieszkańca i sołectwo.");
}

// ==========================================================================
// ASYSTENT AI & POLISH VOICEBOT (Comprehensive Knowledge Engine)
// ==========================================================================
function renderChatMessages() {
  const container = document.getElementById("chat-messages-container");
  if (!container) return;
  container.innerHTML = "";

  state.chatHistory.forEach(msg => {
    const bubble = document.createElement("div");
    bubble.className = `chat-bubble ${msg.sender}`;
    bubble.textContent = msg.text;

    if (msg.recommendations && msg.recommendations.length > 0) {
      msg.recommendations.forEach(rec => {
        const card = document.createElement("div");
        card.className = "recommendation-card";
        card.innerHTML = `<strong>✨ ${rec.title}</strong><div>${rec.sub}</div>`;
        card.onclick = () => switchTab(rec.tab);
        bubble.appendChild(card);
      });
    }
    container.appendChild(bubble);
  });
  container.scrollTop = container.scrollHeight;
}

function clearAiChat() {
  state.chatHistory = [DEFAULT_STATE.chatHistory[0]];
  saveState();
  renderChatMessages();
  showToast("Wyczyszczono historię rozmowy z asystentem.");
}

function askAi(text) {
  document.getElementById("ai-chat-input").value = text;
  sendAiMessage();
}

function sendAiMessage() {
  const input = document.getElementById("ai-chat-input");
  const text = input.value.trim();
  if (!text) return;
  input.value = "";

  // Append User message
  state.chatHistory.push({ sender: "user", text: text });
  renderChatMessages();

  // Voice wave visualizer
  const wave = document.getElementById("audio-wave-visualizer");
  if (wave) wave.classList.add("active");

  setTimeout(() => {
    if (wave) wave.classList.remove("active");
    const response = computeAiResponse(text);
    state.chatHistory.push(response);
    saveState();
    renderChatMessages();

    if (state.user.seniorMode) {
      speakText(response.text);
    }
  }, 450);
}

function computeAiResponse(query) {
  const lower = query.toLowerCase();
  let reply = "";
  let recs = [];

  if (lower.includes("kart") || lower.includes("wkm") || lower.includes("qr") || lower.includes("wyrobić")) {
    reply = "Pani e-Wieliszewska Karta Mieszkańca (WKM) jest w pełni aktywna! Uprawnia do bezpłatnych wejść na warsztaty CUS, zniżki 50% na obiekty sportowe OSiR Wieliszew oraz bezpłatnych przejazdów gminną L-komunikacją. Dynamiczny kod QR odświeża się co 45 sekund dla ochrony danych.";
    recs.push({ title: "Otwórz e-Kartę", sub: "Zobacz dynamiczny QR i kontakty ICE", tab: "pane-card" });
    recs.push({ title: "Moje Zniżki i Vouchery", sub: "Sprawdź pobrane zniżki mieszkańca", tab: "pane-card" });
  } else if (lower.includes("kryter") || lower.includes("dochod") || lower.includes("zasił") || lower.includes("pomoc")) {
    reply = "W 2026 r. kryterium dochodowe w pomocy społecznej wynosi 1 010 zł dla osoby samotnie gospodarującej oraz 776 zł na osobę w rodzinie. CUS Wieliszew przyznaje zasiłki stałe, okresowe i celowe (np. na opał, leki, żywność). Wniosek można omówić z pracownikiem socjalnym podczas dyżuru w urzędzie lub telefonicznie (22 782 01 00).";
    recs.push({ title: "Dyżur Koordynatora CUS", sub: "Zamów rozmowę telefoniczną lub wideo", tab: "pane-telehealth" });
    recs.push({ title: "Magazyn Współdzielenia", sub: "Bezpłatna żywność i sprzęt rehabilitacyjny", tab: "pane-sharing" });
  } else if (lower.includes("wózek") || lower.includes("sprzęt") || lower.includes("chodzik") || lower.includes("kul") || lower.includes("rehabilitac")) {
    reply = "Wypożyczalnia sprzętu rehabilitacyjnego CUS Wieliszew przy ul. Modlińskiej 65 udostępnia bezpłatnie wózki inwalidzkie lekkie, balkoniki z koszykiem oraz kule łokciowe (do 6 miesięcy bez żadnych opłat). Sprzęt można zarezerwować od ręki w aplikacji.";
    recs.push({ title: "Rezerwacja Sprzętu Rehabilitacyjnego", sub: "Dostępne: wózek, balkonik, kule", tab: "pane-sharing" });
  } else if (lower.includes("zakup") || lower.includes("wolontariusz") || lower.includes("sąsiad") || lower.includes("pomóc")) {
    reply = "Na Interaktywnej Mapie Potrzeb można zgłaszać i podejmować drobne formy pomocy sąsiedzkiej (np. wniesienie zgrzewki wody, wykup leków z apteki, transport do przychodni). Za każdą pomoc wolontariusz otrzymuje punkty zaangażowania wymienialne na nagrody!";
    recs.push({ title: "Interaktywna Mapa Potrzeb", sub: "Zobacz 4 aktywne prośby w sołectwach", tab: "pane-map" });
    recs.push({ title: "Lokalny Bank Czasu", sub: "Wymieniaj usługi 1h za 1h", tab: "pane-timebank" });
  } else if (lower.includes("psycholog") || lower.includes("prawnik") || lower.includes("lekarz") || lower.includes("porad")) {
    reply = "W CUS Wieliszew oferujemy bezpłatne, bezpieczne telekonsultacje wideo. Dziś dyżur pełni psycholog mgr Anna Wiśniewska oraz radca prawny mec. Piotr Dąbrowski (pomoc w sprawach majątkowych, socjalnych i opiekuńczych).";
    recs.push({ title: "Połącz ze specjalistą", sub: "Szyfrowana wideorozmowa WebRTC", tab: "pane-telehealth" });
  } else if (lower.includes("jadłodzielni") || lower.includes("jedzeni") || lower.includes("żywnoś") || lower.includes("owoc")) {
    reply = "Gminna Jadłodzielnia Wieliszew znajduje się przy budynku CUS przy ul. Modlińskiej 65 i działa całodobowo (24/7). Każdy mieszkaniec może bezpłatnie poczęstować się świeżą żywnością lub zostawić nadwyżki z własnego ogrodu.";
    recs.push({ title: "Zobacz zasoby Jadłodzielni", sub: "Świeże jabłka i przetwory w punkcie", tab: "pane-sharing" });
  } else {
    reply = "Chętnie pomogę w sprawach Centrum Usług Społecznych Gminy Wieliszew. Mogę sprawdzić Pani e-Kartę, zapisać na gimnastykę \"Zdrowy Kręgosłup 60+\", sprawdzić saldo Banku Czasu lub wezwać pomoc.";
    recs.push({ title: "Gimnastyka Zdrowy Kręgosłup 60+", sub: "Czwartki, Hala Wieliszew", tab: "pane-card" });
    recs.push({ title: "Lokalny Bank Czasu", sub: `Twoje saldo: ${state.user.timeHours} godz.`, tab: "pane-timebank" });
  }

  return { sender: "assistant", text: reply, recommendations: recs };
}

// Text to speech
function speakText(text) {
  if (!("speechSynthesis" in window)) return;
  window.speechSynthesis.cancel();
  const clean = text.replace(/[*_#•🛒🚗🔧☕🐕🌱❤️🤝🏅🏆🩺♻️🚨📶🔍]/g, "");
  const utterance = new SpeechSynthesisUtterance(clean);
  utterance.lang = "pl-PL";
  utterance.rate = state.user.seniorMode ? 0.88 : 1.0;

  const wave = document.getElementById("audio-wave-visualizer");
  utterance.onstart = () => { if (wave) wave.classList.add("active"); };
  utterance.onend = () => { if (wave) wave.classList.remove("active"); };
  utterance.onerror = () => { if (wave) wave.classList.remove("active"); };

  window.speechSynthesis.speak(utterance);
}

// Voice input
function triggerVoiceInput() {
  const btn = document.getElementById("voice-record-btn");
  const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;

  if (SpeechRecognition) {
    const recognition = new SpeechRecognition();
    recognition.lang = "pl-PL";
    recognition.interimResults = false;

    if (btn) btn.classList.add("recording");
    showToast("🎙️ Mów teraz po polsku...");

    recognition.onresult = (e) => {
      if (btn) btn.classList.remove("recording");
      const transcript = e.results[0][0].transcript;
      document.getElementById("ai-chat-input").value = transcript;
      sendAiMessage();
    };
    recognition.onerror = () => {
      if (btn) btn.classList.remove("recording");
      fallbackVoiceSimulation();
    };
    recognition.start();
  } else {
    fallbackVoiceSimulation();
  }
}

function fallbackVoiceSimulation() {
  const simulatedVoice = [
    "Jakie są kryteria dochodowe do zasiłku?",
    "Czy jest dostępny wolny wózek inwalidzki?",
    "Chcę zapisać się na gimnastykę dla seniorów",
    "Gdzie znajduje się Jadłodzielnia Wieliszew?"
  ];
  const chosen = simulatedVoice[Math.floor(Math.random() * simulatedVoice.length)];
  document.getElementById("ai-chat-input").value = chosen;
  showToast(`🎙️ Rozpoznano mowę: "${chosen}"`);
  setTimeout(sendAiMessage, 700);
}

// ==========================================================================
// WYDARZENIA CUS & KALENDARZ (.ICS)
// ==========================================================================
function renderEvents(filterCat = "ALL") {
  const cont = document.getElementById("events-list-container");
  if (!cont) return;
  cont.innerHTML = "";

  const filtered = state.events.filter(e => filterCat === "ALL" || e.category === filterCat);

  filtered.forEach(ev => {
    const isRegistered = state.registeredEvents.includes(ev.id);
    const card = document.createElement("div");
    card.className = "smart-card";
    card.innerHTML = `
      <div style="display: flex; justify-content: space-between; align-items: flex-start;">
        <span class="badge ${isRegistered ? 'badge-green' : 'badge-gold'}">${ev.badge}</span>
        <span class="badge ${isRegistered ? 'badge-green' : 'badge-blue'}">
          ${isRegistered ? '✓ ZAPISANY' : `Miejsca: ${ev.spotsTotal - ev.spotsTaken}`}
        </span>
      </div>
      <h4 style="font-size: 15px; margin: 8px 0 3px 0; font-weight: 800;">${ev.title}</h4>
      <p style="font-size: 12px; color: var(--primary); font-weight: 700;">📅 ${ev.dateText}</p>
      <p style="font-size: 12px; color: var(--text-muted);">📍 ${ev.location}</p>
      <p style="font-size: 12.5px; margin: 6px 0; line-height: 1.4;">${ev.desc}</p>
      <div style="display: flex; gap: 8px; margin-top: 10px;">
        <button class="btn ${isRegistered ? 'btn-secondary' : 'btn-primary'}" onclick="toggleEvent('${ev.id}')">
          ${isRegistered ? 'Wypisz się' : 'Zapisz się z e-Kartą'}
        </button>
        <button class="btn btn-outline" style="width: auto; padding: 0 12px;" onclick="downloadIcsCalendar('${ev.id}')" title="Pobierz przypomnienie do kalendarza">
          📅 .ICS
        </button>
      </div>
    `;
    cont.appendChild(card);
  });
}

function filterEvents(category, btnEl) {
  document.querySelectorAll("#pane-card .chip").forEach(c => c.classList.remove("active"));
  if (btnEl) btnEl.classList.add("active");
  renderEvents(category);
}

function toggleEvent(id) {
  const idx = state.registeredEvents.indexOf(id);
  const ev = state.events.find(e => e.id === id);
  if (idx > -1) {
    state.registeredEvents.splice(idx, 1);
    if (ev) ev.spotsTaken = Math.max(0, ev.spotsTaken - 1);
    showToast(`Zrezygnowano z zapisu na: "${ev.title}".`);
  } else {
    state.registeredEvents.push(id);
    if (ev) ev.spotsTaken = Math.min(ev.spotsTotal, ev.spotsTaken + 1);
    showToast(`🎉 Sukces! Zapisano na: "${ev.title}". Wejściówka dostępna w e-Karcie.`);
    playTone(660, 0.15);
  }
  saveState();
  renderEvents();
}

function downloadIcsCalendar(id) {
  const ev = state.events.find(e => e.id === id);
  if (!ev) return;

  const icsData = [
    "BEGIN:VCALENDAR",
    "VERSION:2.0",
    "PRODID:-//Smart CUS Wieliszew//NONSGML v1.0//PL",
    "BEGIN:VEVENT",
    `SUMMARY:${ev.title}`,
    `DESCRIPTION:${ev.desc} | Prowadzący: ${ev.instructor}`,
    `LOCATION:${ev.location}`,
    "DTSTART:20261112T090000Z",
    "DTEND:20261112T103000Z",
    "STATUS:CONFIRMED",
    "END:VEVENT",
    "END:VCALENDAR"
  ].join("\r\n");

  const blob = new Blob([icsData], { type: "text/calendar;charset=utf-8" });
  const url = URL.createObjectURL(blob);
  const a = document.createElement("a");
  a.href = url;
  a.download = `Wydarzenie_CUS_${id}.ics`;
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);
  URL.revokeObjectURL(url);
  showToast("Pobrano plik kalendarza (.ics) dla wydarzenia!");
}

// ==========================================================================
// VOUCHERS WALLET
// ==========================================================================
function renderVouchers() {
  const countEl = document.getElementById("user-vouchers-count");
  if (countEl) countEl.textContent = state.vouchers.length;

  const cont = document.getElementById("vouchers-list-container");
  if (!cont) return;
  cont.innerHTML = "";

  if (state.vouchers.length === 0) {
    cont.innerHTML = `
      <div class="smart-card" style="text-align: center; padding: 30px 16px;">
        <span style="font-size: 40px;">🎟️</span>
        <h4 style="font-size: 15px; margin: 8px 0 4px 0;">Brak aktywnych voucherów</h4>
        <p style="font-size: 12.5px; color: var(--text-muted); margin-bottom: 14px;">
          Wymieniaj punkty wolontariatu na darmowe kajaki, bilety do teatru czy zniżki w kawiarniach!
        </p>
        <button class="btn btn-primary" onclick="switchTab('pane-gamification')">
          Przejdź do Katalogu Nagród
        </button>
      </div>
    `;
    return;
  }

  state.vouchers.forEach(v => {
    const card = document.createElement("div");
    card.className = "smart-card";
    card.style.borderLeft = "5px solid var(--accent-gold)";
    card.innerHTML = `
      <div style="display: flex; justify-content: space-between;">
        <span class="badge badge-gold">VOUCHER MIESZKAŃCA</span>
        <span class="badge badge-green">${v.status}</span>
      </div>
      <h4 style="font-size: 15px; margin: 6px 0 2px 0;">${v.title}</h4>
      <p style="font-size: 12px; color: var(--primary); font-weight: bold;">Partner: ${v.partner}</p>
      <div style="background: #ffffff; border: 1px dashed #ccc; padding: 10px; border-radius: 10px; text-align: center; margin: 8px 0;">
        <div style="font-size: 16px; font-weight: 900; letter-spacing: 2px; color: var(--primary-dark); font-family: monospace;">
          ${v.code}
        </div>
        <div style="font-size: 10px; color: var(--text-muted); margin-top: 3px;">Pokaż ten kod obsłudze przy kasie</div>
      </div>
      <div style="display: flex; justify-content: space-between; align-items: center; font-size: 11px; color: var(--text-muted);">
        <span>Ważny do: ${v.validUntil}</span>
        <button class="btn btn-secondary btn-sm" onclick="markVoucherUsed('${v.id}')">
          Wykorzystano
        </button>
      </div>
    `;
    cont.appendChild(card);
  });
}

function markVoucherUsed(id) {
  state.vouchers = state.vouchers.filter(v => v.id !== id);
  saveState();
  renderVouchers();
  showToast("Oznaczono voucher jako wykorzystany.");
}

// ==========================================================================
// INTERAKTYWNA MAPA POTRZEB (Leaflet OpenStreetMap)
// ==========================================================================
function initLeafletMap() {
  if (leafletMap) {
    leafletMap.invalidateSize();
    return;
  }

  const mapEl = document.getElementById("leaflet-map");
  if (!mapEl) return;

  // Center on Gmina Wieliszew (52.457°N, 20.965°E)
  leafletMap = L.map("leaflet-map").setView([52.457, 20.965], 12);

  L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    attribution: "© OpenStreetMap contributors | Smart CUS Wieliszew",
    maxZoom: 18
  }).addTo(leafletMap);

  renderLeafletMarkers();
}

function renderLeafletMarkers() {
  if (!leafletMap) return;

  leafletMarkers.forEach(m => leafletMap.removeLayer(m));
  leafletMarkers = [];

  state.mapPins.forEach(pin => {
    const isCompleted = pin.status === "COMPLETED";
    const isInProgress = pin.status === "IN_PROGRESS";
    const markerBg = isCompleted ? "#78909c" : isInProgress ? "#0284c7" : "#137547";

    const customIcon = L.divIcon({
      className: "custom-map-pin",
      html: `<div style="background: ${markerBg}; color: #ffffff; width: 34px; height: 34px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 18px; border: 2px solid #ffffff; box-shadow: 0 4px 10px rgba(0,0,0,0.4);">${pin.icon}</div>`,
      iconSize: [34, 34],
      iconAnchor: [17, 17]
    });

    const m = L.marker([pin.lat, pin.lng], { icon: customIcon }).addTo(leafletMap);
    
    // Draw approximate RODO circle buffer (200m)
    L.circle([pin.lat, pin.lng], {
      radius: 200,
      color: markerBg,
      fillColor: markerBg,
      fillOpacity: 0.15,
      weight: 1
    }).addTo(leafletMap);

    m.bindPopup(`
      <div style="font-family: sans-serif; font-size: 13px; max-width: 200px;">
        <strong style="color: #137547;">${pin.category} (${pin.village})</strong>
        <p style="margin: 4px 0; font-size: 12px;">${pin.title}</p>
        <span style="font-size: 11px; font-weight: bold; color: #f57f17;">Nagroda: +${pin.points} pkt</span><br>
        <button style="background: #137547; color: #fff; border: none; padding: 5px 10px; border-radius: 6px; cursor: pointer; margin-top: 6px; font-weight: bold; width: 100%;" onclick="acceptNeedFromPopup('${pin.id}')">
          ${isCompleted ? "✓ Zakończono" : isInProgress ? "✓ Podjęto pomoc" : "Chcę pomóc"}
        </button>
      </div>
    `);
    leafletMarkers.push(m);
  });
}

function renderNeedPins(filterVillage = "Wszystkie", searchKeyword = "") {
  const list = document.getElementById("need-pins-list");
  if (!list) return;
  list.innerHTML = "";

  const filtered = state.mapPins.filter(p => {
    const matchVillage = filterVillage === "Wszystkie" || p.village === filterVillage;
    const matchSearch = !searchKeyword || p.title.toLowerCase().includes(searchKeyword.toLowerCase()) || p.desc.toLowerCase().includes(searchKeyword.toLowerCase());
    return matchVillage && matchSearch;
  });

  if (filtered.length === 0) {
    list.innerHTML = `<p style="text-align: center; color: var(--text-muted); padding: 20px;">Brak zgłoszeń spełniających kryteria.</p>`;
    return;
  }

  filtered.forEach(pin => {
    const isCompleted = pin.status === "COMPLETED";
    const isInProgress = pin.status === "IN_PROGRESS";

    const card = document.createElement("div");
    card.className = "smart-card";
    card.innerHTML = `
      <div style="display: flex; justify-content: space-between; align-items: center;">
        <span class="badge ${isCompleted ? 'badge-blue' : isInProgress ? 'badge-blue' : 'badge-gold'}">
          ${pin.icon} ${pin.category} • ${pin.village}
        </span>
        <span class="badge ${isCompleted ? 'badge-green' : isInProgress ? 'badge-blue' : 'badge-red'}">
          ${isCompleted ? 'ZREALIZOWANE' : isInProgress ? 'W REALIZACJI' : pin.urgency}
        </span>
      </div>
      <h4 style="font-size: 15px; margin: 8px 0 4px 0; font-weight: 800;">${pin.title}</h4>
      <p style="font-size: 12px; color: var(--primary); font-weight: 700;">📍 ${pin.address}</p>
      <p style="font-size: 12.5px; margin: 6px 0; line-height: 1.4;">${pin.desc}</p>
      <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 10px;">
        <span style="font-weight: 900; color: #f57f17; font-size: 13px;">+${pin.points} pkt wolontariatu</span>
        <div style="display: flex; gap: 6px;">
          ${isInProgress ? `
            <button class="btn btn-primary btn-sm" onclick="completeNeed('${pin.id}')">
              ✓ Potwierdź wykonanie
            </button>
          ` : `
            <button class="btn ${isCompleted ? 'btn-secondary' : 'btn-primary'} btn-sm" onclick="acceptNeed('${pin.id}')" ${isCompleted ? 'disabled' : ''}>
              ${isCompleted ? '✓ Zrealizowano' : 'Chcę pomóc'}
            </button>
          `}
        </div>
      </div>
    `;
    list.appendChild(card);
  });
}

function filterMapVillage(village, el) {
  document.querySelectorAll("#pane-map .chip").forEach(c => c.classList.remove("active"));
  if (el) el.classList.add("active");
  renderNeedPins(village);

  if (leafletMap) {
    const coords = {
      "Wieliszew": [52.457, 20.978],
      "Skrzeszew": [52.463, 20.925],
      "Michałów-Reginów": [52.433, 20.982],
      "Łajski": [52.428, 20.950],
      "Komornica": [52.470, 20.985],
      "Kałuszyn": [52.468, 20.910],
      "Góra": [52.490, 20.890]
    };
    if (coords[village]) {
      leafletMap.flyTo(coords[village], 14);
    } else {
      leafletMap.flyTo([52.457, 20.965], 12);
    }
  }
}

function filterMapBySearch(keyword) {
  renderNeedPins("Wszystkie", keyword);
}

function acceptNeed(id) {
  const pin = state.mapPins.find(p => p.id === id);
  if (!pin) return;
  pin.status = "IN_PROGRESS";
  saveState();
  renderNeedPins();
  renderLeafletMarkers();
  showToast(`🤝 Podjęto zadanie: "${pin.title}". Dane koordynatora przekazane.`);
  playTone(550, 0.15);
}

function completeNeed(id) {
  const pin = state.mapPins.find(p => p.id === id);
  if (!pin) return;
  pin.status = "COMPLETED";
  state.user.points += pin.points;

  // Check badges unlock
  checkBadgesUnlock();

  saveState();
  renderNeedPins();
  renderLeafletMarkers();
  renderGamification();
  showToast(`🎉 Brawo! Ukończono zadanie sąsiedzkie. Przyznano +${pin.points} punktów aktywności.`);
  playTone(880, 0.2);
}

function acceptNeedFromPopup(id) {
  acceptNeed(id);
  if (leafletMap) leafletMap.closePopup();
}

function openAddNeedModal() {
  document.getElementById("modal-add-need").classList.add("active");
}

function submitNewNeed() {
  const title = document.getElementById("new-need-title").value.trim();
  const desc = document.getElementById("new-need-desc").value.trim();
  const village = document.getElementById("new-need-village").value;
  const category = document.getElementById("new-need-category").value;

  if (!title) {
    alert("Proszę wpisać tytuł potrzeby.");
    return;
  }

  const icons = { "Zakupy": "🛒", "Transport": "🚗", "Drobna naprawa": "🔧", "Opieka": "🐕", "Ogród": "🌱" };

  const newPin = {
    id: `pin-${Date.now()}`,
    title: title,
    category: category,
    icon: icons[category] || "🤝",
    urgency: "Pilne",
    village: village,
    address: `Rejon sołectwa ${village} (strefa zanonimizowana)`,
    desc: desc || "Potrzeba wsparcia zgłoszona przez mieszkańca.",
    points: 40,
    lat: 52.450 + (Math.random() - 0.5) * 0.03,
    lng: 20.960 + (Math.random() - 0.5) * 0.05,
    status: "OPEN"
  };

  state.mapPins.unshift(newPin);
  saveState();
  closeModals();
  renderNeedPins();
  renderLeafletMarkers();
  showToast("Opublikowano nowe zapotrzebowanie na Mapie Potrzeb.");
}

// ==========================================================================
// BANK CZASU (1h za 1h)
// ==========================================================================
function renderTimeBank(filterType = "ALL") {
  const balEl = document.getElementById("timebank-balance");
  if (balEl) balEl.textContent = `${state.user.timeHours.toFixed(1)} godz.`;

  const list = document.getElementById("timebank-items-list");
  if (!list) return;
  list.innerHTML = "";

  const filtered = state.timeBank.filter(i => filterType === "ALL" || i.type === filterType);

  filtered.forEach(item => {
    const isOffer = item.type === "OFFERING";
    const card = document.createElement("div");
    card.className = "smart-card";
    card.innerHTML = `
      <div style="display: flex; justify-content: space-between; align-items: center;">
        <span class="badge ${isOffer ? 'badge-green' : 'badge-blue'}">
          ${isOffer ? 'MIESZKANIEC OFERUJE' : 'POSZUKIWANA POMOC'}
        </span>
        <span style="font-weight: 900; color: var(--primary);">⏳ ${item.hours}h</span>
      </div>
      <h4 style="font-size: 15px; margin: 8px 0 2px 0; font-weight: 800;">${item.title}</h4>
      <p style="font-size: 12px; color: var(--text-muted);">${item.author} • ${item.rating}</p>
      <p style="font-size: 12.5px; margin: 6px 0; line-height: 1.4;">${item.desc}</p>
      <button class="btn ${isOffer ? 'btn-primary' : 'btn-outline'}" style="margin-top: 8px;" onclick="exchangeTimeBank('${item.id}')">
        ${isOffer ? `Wymień ${item.hours}h ze swojego portfela` : `Zaoferuj pomoc (Zarób +${item.hours}h)`}
      </button>
    `;
    list.appendChild(card);
  });

  renderTimeBankHistory();
}

function filterTimeBank(type, el) {
  document.querySelectorAll("#pane-timebank .chip").forEach(c => c.classList.remove("active"));
  if (el) el.classList.add("active");
  renderTimeBank(type);
}

function toggleTimeBankHistory() {
  const box = document.getElementById("timebank-history-box");
  if (box) box.style.display = box.style.display === "none" ? "block" : "none";
}

function renderTimeBankHistory() {
  const list = document.getElementById("timebank-history-list");
  if (!list) return;
  list.innerHTML = "";

  state.timeBankHistory.forEach(h => {
    const li = document.createElement("li");
    li.style.borderBottom = "1px solid #e2e8f0";
    li.style.paddingBottom = "4px";
    li.innerHTML = `
      <div style="display: flex; justify-content: space-between; font-weight: bold;">
        <span>${h.text}</span>
        <span style="color: ${h.change.startsWith('+') ? 'green' : 'red'};">${h.change}</span>
      </div>
      <div style="display: flex; justify-content: space-between; color: var(--text-muted); font-size: 11px;">
        <span>${h.date}</span>
        <span>Saldo: ${h.balance}</span>
      </div>
    `;
    list.appendChild(li);
  });
}

function exchangeTimeBank(id) {
  const item = state.timeBank.find(i => i.id === id);
  if (!item) return;

  if (item.type === "OFFERING") {
    if (state.user.timeHours >= item.hours) {
      state.user.timeHours -= item.hours;
      state.timeBankHistory.unshift({
        id: `h-${Date.now()}`,
        date: "Dzisiaj, przed chwilą",
        text: `Skorzystano: ${item.title}`,
        change: `-${item.hours}h`,
        balance: `${state.user.timeHours.toFixed(1)}h`
      });
      showToast(`Pobrano ${item.hours}h z portfela. Skontaktowano z wykonawcą.`);
      playTone(500, 0.15);
    } else {
      showToast("Brak wystarczającej liczby godzin w portfelu. Zaoferuj swoją pomoc innemu sąsiadowi!");
      return;
    }
  } else {
    state.user.timeHours += item.hours;
    state.timeBankHistory.unshift({
      id: `h-${Date.now()}`,
      date: "Dzisiaj, przed chwilą",
      text: `Przyjęto zadanie: ${item.title}`,
      change: `+${item.hours}h`,
      balance: `${state.user.timeHours.toFixed(1)}h`
    });
    showToast(`Przyjęto zadanie! Po realizacji otrzymasz +${item.hours}h do portfela.`);
    playTone(700, 0.15);
  }

  saveState();
  renderTimeBank();
}

function openAddTimeBankModal() {
  document.getElementById("modal-add-timebank").classList.add("active");
}

function submitNewTimeBank() {
  const title = document.getElementById("new-tb-title").value.trim();
  const desc = document.getElementById("new-tb-desc").value.trim();
  const type = document.getElementById("new-tb-type").value;

  if (!title) {
    alert("Proszę wpisać nazwę umiejętności.");
    return;
  }

  state.timeBank.unshift({
    id: `tb-${Date.now()}`,
    title: (type === "OFFERING" ? "Oferuję: " : "Poszukuję: ") + title,
    author: `${state.user.name} (${state.user.village})`,
    type: type,
    hours: 1.0,
    desc: desc || "Wymiana w Banku Czasu Gminy Wieliszew.",
    rating: "⭐ Nowy profil"
  });

  saveState();
  closeModals();
  renderTimeBank();
  showToast("Opublikowano wpis w Banku Czasu!");
}

// ==========================================================================
// GRYWALIZACJA, ODZNAKI I VOUCHERY
// ==========================================================================
function renderGamification() {
  // Points & Level Title
  const pDisp = document.getElementById("gamification-points-display");
  if (pDisp) pDisp.textContent = `⭐ ${state.user.points} punktów aktywności`;

  const lvlTitle = document.getElementById("user-level-title");
  let level = "Aktywny Mieszkaniec";
  let target = 600;
  if (state.user.points >= 600) {
    level = "Lider Społeczności Wieliszewa";
    target = 1000;
  } else if (state.user.points >= 350) {
    level = "Zasłużony Sąsiad Wieliszewa";
    target = 600;
  }
  if (lvlTitle) lvlTitle.textContent = level;

  const bar = document.getElementById("level-progress-bar");
  if (bar) {
    const percent = Math.min(100, (state.user.points / target) * 100);
    bar.style.width = `${percent}%`;
  }

  // Badges grid
  const bGrid = document.getElementById("badges-grid-container");
  if (bGrid) {
    bGrid.innerHTML = "";
    state.badges.forEach(b => {
      const card = document.createElement("div");
      card.className = `badge-item-card ${b.unlocked ? '' : 'locked'}`;
      card.onclick = () => openBadgeModal(b);
      card.innerHTML = `
        <div style="font-size: 32px;">${b.emoji}</div>
        <strong style="font-size: 11px; display: block; margin-top: 4px;">${b.title}</strong>
        <span style="font-size: 9.5px; color: ${b.unlocked ? '#2e7d32' : '#888'};">
          ${b.unlocked ? 'Odblokowana' : 'Zablokowana'}
        </span>
      `;
      bGrid.appendChild(card);
    });
  }

  // Rewards catalog
  const rCont = document.getElementById("rewards-list-container");
  if (rCont) {
    rCont.innerHTML = "";
    state.rewards.forEach(rew => {
      const canAfford = state.user.points >= rew.cost;
      const card = document.createElement("div");
      card.className = "smart-card";
      card.innerHTML = `
        <div style="display: flex; justify-content: space-between;">
          <span class="badge badge-gold">${rew.category}</span>
          <strong style="color: #f57f17; font-size: 14px;">${rew.cost} pkt</strong>
        </div>
        <h4 style="font-size: 15px; margin: 6px 0 2px 0; font-weight: 800;">${rew.title}</h4>
        <p style="font-size: 12px; color: var(--primary); font-weight: 700;">Partner: ${rew.partner}</p>
        <p style="font-size: 12.5px; margin: 4px 0 10px 0; line-height: 1.4;">${rew.desc}</p>
        <button class="btn ${canAfford ? 'btn-primary' : 'btn-secondary'}" onclick="redeemReward('${rew.id}')" ${canAfford ? '' : 'disabled'}>
          ${canAfford ? `Odbierz voucher (${rew.cost} pkt)` : `Brakuje ${rew.cost - state.user.points} pkt`}
        </button>
      `;
      rCont.appendChild(card);
    });
  }
}

function openBadgeModal(badge) {
  document.getElementById("badge-modal-icon").textContent = badge.emoji;
  document.getElementById("badge-modal-title").textContent = badge.title;
  const statusEl = document.getElementById("badge-modal-status");
  statusEl.textContent = badge.unlocked ? "ODBLOKOWANA ✓" : "ZABLOKOWANA";
  statusEl.className = badge.unlocked ? "badge badge-green" : "badge badge-gold";
  document.getElementById("badge-modal-desc").textContent = badge.desc;
  document.getElementById("modal-badge-detail").classList.add("active");
}

function redeemReward(rewardId) {
  const rew = state.rewards.find(r => r.id === rewardId);
  if (!rew) return;

  if (state.user.points >= rew.cost) {
    state.user.points -= rew.cost;

    // Generate unique Voucher Code
    const code = `WKM-${Math.floor(1000 + Math.random() * 9000)}-${Date.now().toString().slice(-4)}`;
    const newVoucher = {
      id: `vouch-${Date.now()}`,
      title: rew.title,
      partner: rew.partner,
      code: code,
      validUntil: "31.12.2026",
      status: "AKTYWNY"
    };

    state.vouchers.unshift(newVoucher);
    saveState();
    renderGamification();
    renderVouchers();

    showToast(`🎉 Brawo! Odebrano voucher. Kod "${code}" zapisano w Twojej e-Karcie!`);
    playTone(880, 0.25);
  } else {
    showToast(`Brakuje Ci punktów! Wymagane: ${rew.cost} pkt.`);
  }
}

function checkBadgesUnlock() {
  if (state.user.points >= 600) {
    const b = state.badges.find(x => x.id === "b-6");
    if (b) b.unlocked = true;
  }
}

// ==========================================================================
// TELEOPIEKA & SZYFROWANE POLACZENIA WIDEO (WebRTC)
// ==========================================================================
function renderSpecialists() {
  const cont = document.getElementById("specialists-list-container");
  if (!cont) return;
  cont.innerHTML = "";

  state.specialists.forEach(sp => {
    const card = document.createElement("div");
    card.className = "smart-card";
    card.innerHTML = `
      <div style="display: flex; justify-content: space-between;">
        <span class="badge ${sp.today ? 'badge-green' : 'badge-gold'}">
          ${sp.today ? 'Dyżur Dzisiaj' : 'Zapisy na termin'}
        </span>
        <span class="badge badge-blue">${sp.role.split(" ")[0]}</span>
      </div>
      <h4 style="font-size: 15px; margin: 8px 0 2px 0; font-weight: 800;">${sp.name}</h4>
      <p style="font-size: 12px; color: var(--primary); font-weight: 700;">🕒 ${sp.slot}</p>
      <p style="font-size: 12.5px; margin: 6px 0; line-height: 1.4;">${sp.desc}</p>
      <div style="display: flex; gap: 8px; margin-top: 10px;">
        ${sp.today ? `
          <button class="btn btn-primary" onclick="startVideoCall('${sp.name}', '${sp.role}')">
            🎥 Połącz wideo teraz
          </button>
        ` : `
          <button class="btn btn-secondary" onclick="openConsultationBooking('${sp.name}', '${sp.role}')">
            📅 Zarezerwuj termin
          </button>
        `}
      </div>
    `;
    cont.appendChild(card);
  });
}

function startVideoCall(name, role) {
  document.getElementById("video-specialist-name").textContent = name;
  document.getElementById("video-specialist-role").textContent = role;
  document.getElementById("video-main-label").textContent = name;
  document.getElementById("video-call-screen").classList.add("active");

  // Attempt local camera preview
  const videoEl = document.getElementById("local-video-preview");
  const fallbackEl = document.getElementById("local-video-fallback");

  if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
    navigator.mediaDevices.getUserMedia({ video: true, audio: false })
      .then(stream => {
        localCameraStream = stream;
        if (videoEl) {
          videoEl.srcObject = stream;
          videoEl.style.display = "block";
        }
        if (fallbackEl) fallbackEl.style.display = "none";
      })
      .catch(() => {
        if (videoEl) videoEl.style.display = "none";
        if (fallbackEl) fallbackEl.style.display = "block";
      });
  }

  callDuration = 0;
  clearInterval(callInterval);
  callInterval = setInterval(() => {
    callDuration++;
    const mm = String(Math.floor(callDuration / 60)).padStart(2, "0");
    const ss = String(callDuration % 60).padStart(2, "0");
    const timer = document.getElementById("call-timer");
    if (timer) timer.textContent = `LIVE ${mm}:${ss}`;
  }, 1000);

  playTone(440, 0.2);
}

function toggleCallMic() {
  const btn = document.getElementById("btn-toggle-mic");
  btn.classList.toggle("call-btn-danger");
  showToast("Przełączono stan mikrofonu");
}

function toggleCallCam() {
  const btn = document.getElementById("btn-toggle-cam");
  btn.classList.toggle("call-btn-danger");
  showToast("Przełączono stan kamery");
}

function endVideoCall() {
  clearInterval(callInterval);
  if (localCameraStream) {
    localCameraStream.getTracks().forEach(t => t.stop());
    localCameraStream = null;
  }
  document.getElementById("video-call-screen").classList.remove("active");
  showToast("Zakończono szyfrowaną konsultację ze specjalistą CUS.");
}

function openConsultationBooking(name, role) {
  document.getElementById("booking-specialist-info").textContent = `${name} (${role})`;
  document.getElementById("modal-book-consultation").classList.add("active");
}

function confirmConsultationBooking() {
  const day = document.getElementById("booking-day-select").value;
  closeModals();
  showToast(`📅 Zarezerwowano wizytę na: ${day}. Potwierdzenie wysłano do CUS.`);
}

// ==========================================================================
// WSPÓŁDZIELENIE & JADŁODZIELNIA
// ==========================================================================
function renderSharingItems(filterCategory = "ALL") {
  const cont = document.getElementById("sharing-items-container");
  if (!cont) return;
  cont.innerHTML = "";

  const filtered = state.sharingItems.filter(i => {
    if (filterCategory === "ALL") return true;
    if (filterCategory === "Sprzęt Rehabilitacyjny") return i.category === "Sprzęt Rehabilitacyjny";
    if (filterCategory === "Jadłodzielnia") return i.category.includes("Jadłodzielnia");
    return i.category === "Ubrania i Książki";
  });

  filtered.forEach(item => {
    const card = document.createElement("div");
    card.className = "smart-card";
    card.innerHTML = `
      <div style="display: flex; justify-content: space-between;">
        <span class="badge badge-green">${item.category}</span>
        <span class="badge ${item.reserved ? 'badge-gold' : 'badge-green'}">
          ${item.reserved ? 'ZAREZERWOWANY' : 'BEZPŁATNY'}
        </span>
      </div>
      <h4 style="font-size: 15px; margin: 8px 0 2px 0; font-weight: 800;">${item.title}</h4>
      <p style="font-size: 12px; color: var(--primary); font-weight: 700;">📍 ${item.location}</p>
      <p style="font-size: 12px; color: var(--text-muted);">${item.status}</p>
      <p style="font-size: 12.5px; margin: 6px 0; line-height: 1.4;">${item.desc}</p>
      <button class="btn ${item.reserved ? 'btn-secondary' : 'btn-primary'}" style="margin-top: 8px;" onclick="toggleReserveSharing('${item.id}')">
        ${item.reserved ? `✓ Zarezerwowano (Kod: ${item.reserveCode || 'CUS-491'})` : 'Zarezerwuj do bezpłatnego odbioru'}
      </button>
    `;
    cont.appendChild(card);
  });
}

function filterSharing(category, el) {
  document.querySelectorAll("#pane-sharing .chip").forEach(c => c.classList.remove("active"));
  if (el) el.classList.add("active");
  renderSharingItems(category);
}

function toggleReserveSharing(id) {
  const item = state.sharingItems.find(i => i.id === id);
  if (!item) return;

  item.reserved = !item.reserved;
  if (item.reserved) {
    item.reserveCode = `CUS-ODBIOR-${Math.floor(100 + Math.random() * 900)}`;
    showToast(`🎉 Zarezerwowano: "${item.title}". Twój kod odbioru: ${item.reserveCode}.`);
    playTone(660, 0.15);
  } else {
    item.reserveCode = null;
    showToast("Anulowano rezerwację przedmiotu.");
  }

  saveState();
  renderSharingItems();
}

function openDonateModal() {
  document.getElementById("modal-donate").classList.add("active");
}

function submitDonation() {
  const title = document.getElementById("donate-title").value.trim();
  const category = document.getElementById("donate-category").value;
  const location = document.getElementById("donate-location").value.trim();
  const desc = document.getElementById("donate-desc").value.trim();

  if (!title) {
    alert("Proszę wpisać nazwę przedmiotu lub żywności.");
    return;
  }

  const newItem = {
    id: `sh-${Date.now()}`,
    title: title,
    category: category,
    location: location || "Punkt CUS Wieliszew",
    status: "Dostępne od ręki (Dar mieszkańca)",
    desc: desc || "Zgłoszone w ramach solidarności sąsiedzkiej.",
    reserved: false,
    reserveCode: null
  };

  state.sharingItems.unshift(newItem);
  state.user.points += 30; // Volunteer points reward
  saveState();
  closeModals();
  renderSharingItems();
  renderGamification();
  showToast(`Dziękujemy! Dodano darowiznę do magazynu. Przyznano +30 pkt wolontariatu.`);
}

// ==========================================================================
// MODALS MANAGEMENT
// ==========================================================================
function closeModals() {
  document.querySelectorAll(".modal-overlay").forEach(m => m.classList.remove("active"));
}
