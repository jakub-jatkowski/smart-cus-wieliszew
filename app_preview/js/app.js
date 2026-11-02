/**
 * Smart CUS Wieliszew - Interactive Android Application Simulator
 * Complete Multi-Module Social Services Hub (Gmina Wieliszew)
 */

// Application State
const state = {
  user: {
    name: "Janina Kowalska",
    village: "Wieliszew",
    cardNum: "WKM-2026-88492",
    timeHours: 5.5,
    points: 420,
    seniorMode: false
  },
  totpSeconds: 45,
  registeredEvents: [1],
  mapPins: [
    {
      id: "pin-1",
      title: "Wniesienie zgrzewki wody i leków z apteki",
      category: "Zakupy",
      icon: "🛒",
      urgency: "Pilne (dzisiaj)",
      village: "Wieliszew",
      address: "Rejon ul. Kościelnej / Park Świętojański",
      desc: "Pani Maria (82 l.) po operacji kolana potrzebuje pomocy w odbiorze leków z apteki i wniesieniu 2 zgrzewek wody do mieszkania.",
      points: 50,
      lat: 52.4572,
      lng: 20.9785,
      accepted: false
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
      accepted: false
    },
    {
      id: "pin-3",
      title: "Pomoc w konfiguracji nowego dekodera TV",
      category: "Drobna naprawa",
      icon: "🔧",
      urgency: "Elastyczny termin",
      village: "Michałów-Reginów",
      address: "Okolice stacji PKP Michałów-Reginów",
      desc: "Podłączenie kabla HDMI i przeskanowanie nowych kanałów telewizji naziemnej dla starszego małżeństwa.",
      points: 45,
      lat: 52.4330,
      lng: 20.9820,
      accepted: false
    },
    {
      id: "pin-4",
      title: "Wyprowadzenie psa rasy Beagle po południu",
      category: "Opieka",
      icon: "🐕",
      urgency: "Pilne (dzisiaj)",
      village: "Łajski",
      address: "Rejon ul. Sikorskiego",
      desc: "Krótki 30-minutowy spacer z łagodnym psem. Właścicielka leży z grypą i potrzebuje wsparcia.",
      points: 35,
      lat: 52.4280,
      lng: 20.9500,
      accepted: false
    }
  ],
  timeBank: [
    {
      id: "tb-1",
      title: "Oferuję: Naprawy hydrauliczne i złota rączka",
      author: "Tadeusz N. (Wieliszew)",
      type: "OFFERING",
      hours: 1.0,
      desc: "Wymiana uszczelek, naprawa kranu, regulacja okien. Chętnie pomogę w zamian za 1h w banku czasu.",
      rating: "⭐ 5.0"
    },
    {
      id: "tb-2",
      title: "Oferuję: Nauka obsługi smartfona i komputera",
      author: "Magdalena K. (Michałów-Reginów)",
      type: "OFFERING",
      hours: 1.0,
      desc: "Spokojna, cierpliwa nauka pisania wiadomości, wideorozmów z rodziną, bezpiecznego logowania do e-Karty.",
      rating: "⭐ 4.9"
    },
    {
      id: "tb-3",
      title: "Poszukuję: Zgrabienie liści w ogródku przed zimą",
      author: "Helena W. (Skrzeszew)",
      type: "REQUESTING",
      hours: 2.0,
      desc: "Niewielki ogródek. Zapewniam grabie, worki, ciepłą herbatę i domowe ciasto drożdżowe!",
      rating: "⭐ 4.8"
    }
  ],
  sharingItems: [
    {
      id: "sh-1",
      title: "Wózek inwalidzki lekki aluminiowy (składany)",
      category: "Sprzęt Rehabilitacyjny",
      location: "Punkt CUS Wieliszew, ul. Modlińska 65",
      status: "Dostępny od ręki (2 szt.)",
      desc: "Zdezynfekowany, po przeglądzie technicznym. Bezpłatne wypożyczenie z e-Kartą.",
      reserved: false
    },
    {
      id: "sh-2",
      title: "Balkonik czterokołowy z koszykiem na zakupy",
      category: "Sprzęt Rehabilitacyjny",
      location: "Świetlica w Skrzeszewie",
      status: "Dostępny od ręki (1 szt.)",
      desc: "Lekka konstrukcja, blokada kół, wygodne siedzisko do odpoczynku na spacerze.",
      reserved: false
    },
    {
      id: "sh-3",
      title: "Świeże jabłka i przetwory (Jadłodzielnia)",
      category: "Jadłodzielnia Wieliszew",
      location: "Punkt Jadłodzielni przy CUS (całodobowy)",
      status: "Dostępne (8 kg)",
      desc: "Nadwyżka od lokalnego sadownika z Kałuszyna. Zapraszamy do bezpłatnego częstowania się!",
      reserved: false
    },
    {
      id: "sh-4",
      title: "Kule łokciowe z regulacją wysokości (para)",
      category: "Sprzęt Rehabilitacyjny",
      location: "Punkt CUS Wieliszew",
      status: "Dostępne (3 pary)",
      desc: "Ergonomiczny uchwyt, odblaskowe elementy bezpieczeństwa.",
      reserved: false
    }
  ]
};

let leafletMap = null;
let leafletMarkers = [];
let callInterval = null;
let callDuration = 0;

// Initialize on Load
document.addEventListener("DOMContentLoaded", () => {
  initClock();
  initTotpTimer();
  renderNeedPins();
  renderTimeBank();
  renderSharingItems();
  initPwaInstall();
});

// PWA Service Worker Registration & Install Prompt
let deferredPrompt = null;
function initPwaInstall() {
  if ('serviceWorker' in navigator) {
    navigator.serviceWorker.register('./sw.js').catch(err => console.log('SW registration error:', err));
  }
  window.addEventListener('beforeinstallprompt', (e) => {
    e.preventDefault();
    deferredPrompt = e;
    showToast("📱 Możesz zainstalować Smart CUS na swoim telefonie!");
  });
}

function promptInstallApp() {
  if (deferredPrompt) {
    deferredPrompt.prompt();
    deferredPrompt.userChoice.then(() => { deferredPrompt = null; });
  } else {
    showToast("Aby zainstalować: w menu Chrome kliknij 'Dodaj do ekranu głównego'");
  }
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

// TOTP Dynamic QR Code Progress
function initTotpTimer() {
  const bar = document.getElementById("totp-progress-bar");
  const sec = document.getElementById("totp-seconds");
  const svg = document.getElementById("qr-code-svg");

  setInterval(() => {
    state.totpSeconds--;
    if (state.totpSeconds <= 0) {
      state.totpSeconds = 45;
      // Slight visual rotation of dynamic hash dots
      if (svg) {
        const randX = 50 + Math.floor(Math.random() * 50);
        const randY = 50 + Math.floor(Math.random() * 50);
        svg.querySelector("circle").setAttribute("cx", randX);
        svg.querySelector("circle").setAttribute("cy", randY);
      }
    }
    if (sec) sec.textContent = state.totpSeconds;
    if (bar) bar.style.width = `${(state.totpSeconds / 45) * 100}%`;
  }, 1000);
}

// Tab Switching
function switchTab(paneId, btnElement) {
  document.querySelectorAll(".tab-pane").forEach(p => p.classList.remove("active"));
  const target = document.getElementById(paneId);
  if (target) target.classList.add("active");

  if (btnElement) {
    document.querySelectorAll(".nav-item").forEach(b => b.classList.remove("active"));
    btnElement.classList.add("active");
  }

  // Lazy initialize map if Map tab opened
  if (paneId === "pane-map") {
    setTimeout(initLeafletMap, 150);
  }
}

// Senior Mode Toggle (WCAG 2.1 AA)
function toggleSeniorMode() {
  state.user.seniorMode = !state.user.seniorMode;
  document.body.classList.toggle("senior-mode", state.user.seniorMode);

  const btnPill = document.getElementById("btn-toggle-senior");
  if (btnPill) {
    btnPill.classList.toggle("active", state.user.seniorMode);
    btnPill.innerHTML = state.user.seniorMode
      ? "<span>👁️</span> Tryb Seniora: WŁĄCZONY (Wysoki Kontrast)"
      : "<span>👁️</span> Przełącz Tryb Seniora / WCAG 2.1 AA";
  }

  showToast(
    state.user.seniorMode
      ? "Włączono Tryb Seniora: powiększone czcionki, wysoki kontrast WCAG 2.1 AA"
      : "Przywrócono standardowy tryb wyświetlania"
  );
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
    }, 3200);
  }
}

// NFC Emulation Tap
function simulateNfcTap() {
  playTone(880, 0.15);
  setTimeout(() => playTone(1200, 0.2), 160);
  showToast("📶 Pomyślnie zweryfikowano e-Kartę przez NFC (Czytnik w Hali Wieliszew)");
}

// Audio tone synthesizer for NFC/voice feedback
function playTone(freq, dur) {
  try {
    const ctx = new (window.AudioContext || window.webkitAudioContext)();
    const osc = ctx.createOscillator();
    const gain = ctx.createGain();
    osc.frequency.value = freq;
    osc.type = "sine";
    gain.gain.setValueAtTime(0.1, ctx.currentTime);
    gain.gain.exponentialRampToValueAtTime(0.001, ctx.currentTime + dur);
    osc.connect(gain);
    gain.connect(ctx.destination);
    osc.start();
    osc.stop(ctx.currentTime + dur);
  } catch (e) {
    // AudioContext blocked or unsupported
  }
}

// Speech Synthesis (Polish Voicebot)
function speakText(text) {
  if (!("speechSynthesis" in window)) return;
  window.speechSynthesis.cancel();
  const clean = text.replace(/[*_#•🛒🚗🔧☕🐕🌱❤️🤝🏅🏆🩺♻️]/g, "");
  const utterance = new SpeechSynthesisUtterance(clean);
  utterance.lang = "pl-PL";
  utterance.rate = state.user.seniorMode ? 0.9 : 1.0;
  window.speechSynthesis.speak(utterance);
}

function speakCurrentAssistantMessage() {
  const bubbles = document.querySelectorAll(".chat-bubble.assistant");
  if (bubbles.length > 0) {
    const last = bubbles[bubbles.length - 1];
    speakText(last.innerText);
    showToast("🔊 Odtwarzanie głosu Voicebota CUS...");
  }
}

// Speech Recognition (Voice Input)
function triggerVoiceInput() {
  const btn = document.getElementById("voice-record-btn");
  const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;

  if (SpeechRecognition) {
    const recognition = new SpeechRecognition();
    recognition.lang = "pl-PL";
    recognition.interimResults = false;

    if (btn) btn.classList.add("recording");
    showToast("🎙️ Mów teraz do mikrofonu po polsku...");

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
    "Jak zapisać się na zajęcia z kręgosłupa?",
    "Czy jest dostępny wolny wózek inwalidzki?",
    "Chcę pomóc sąsiadowi w zakupach"
  ];
  const chosen = simulatedVoice[Math.floor(Math.random() * simulatedVoice.length)];
  document.getElementById("ai-chat-input").value = chosen;
  showToast(`🎙️ Rozpoznano głos: "${chosen}"`);
  setTimeout(sendAiMessage, 800);
}

// AI Chatbot Flow
function askAi(text) {
  document.getElementById("ai-chat-input").value = text;
  sendAiMessage();
}

function sendAiMessage() {
  const input = document.getElementById("ai-chat-input");
  const text = input.value.trim();
  if (!text) return;
  input.value = "";

  const container = document.getElementById("chat-messages-container");

  // Append User Bubble
  const userDiv = document.createElement("div");
  userDiv.className = "chat-bubble user";
  userDiv.textContent = text;
  container.appendChild(userDiv);
  container.scrollTop = container.scrollHeight;

  // Generate Smart Assistant Answer
  setTimeout(() => {
    const botDiv = document.createElement("div");
    botDiv.className = "chat-bubble assistant";

    const lower = text.toLowerCase();
    let reply = "";
    let recommendations = [];

    if (lower.includes("kart") || lower.includes("wkm") || lower.includes("qr")) {
      reply = "Pani e-Wieliszewska Karta Mieszkańca jest aktywna! Kod QR w aplikacji odświeża się co 45 sekund, co chroni Pani dane. Może Pani również używać technologii zbliżeniowej NFC.";
      recommendations.push({ title: "Otwórz e-Kartę", sub: "Zobacz dynamiczny QR i ulgi", tab: "pane-card" });
    } else if (lower.includes("wózek") || lower.includes("sprzęt") || lower.includes("rehabilitac") || lower.includes("chodzik")) {
      reply = "W naszym magazynie cyrkularnym CUS przy ul. Modlińskiej mamy bezpłatnie dostępny wózek inwalidzki lekki oraz balkonik. Można go bez opłat zarezerwować w module Współdzielenia.";
      recommendations.push({ title: "Zarezerwuj sprzęt rehabilitacyjny", sub: "Bezpłatne wypożyczenie do 6 miesięcy", tab: "pane-sharing" });
    } else if (lower.includes("zakup") || lower.includes("pomoc") || lower.includes("wolontariusz")) {
      reply = "Na naszej Interaktywnej Mapie Potrzeb mieszkańcy mogą zgłaszać i podejmować drobne formy pomocy (np. zakupy, podwiezienie). Sprawdźmy pinezki w Pani okolicy.";
      recommendations.push({ title: "Interaktywna Mapa Potrzeb", sub: "Sprawdź 4 zgłoszenia w sołectwach", tab: "pane-map" });
    } else if (lower.includes("psycholog") || lower.includes("prawnik") || lower.includes("rozmow") || lower.includes("doktor")) {
      reply = "W CUS Wieliszew świadczymy bezpieczne telekonsultacje wideo. Dzisiaj dyżur pełni psycholog mgr Anna Wiśniewska oraz radca prawny mec. Piotr Dąbrowski.";
      recommendations.push({ title: "Połącz ze specjalistą", sub: "Szyfrowana wideorozmowa bez wychodzenia z domu", tab: "pane-telehealth" });
    } else {
      reply = `Chętnie pomogę w sprawach CUS Wieliszew. Mogę zapisać Panią na warsztaty zdrowotne, sprawdzić stan punktów w Banku Czasu lub pokazać dostępne zniżki gminne.`;
      recommendations.push({ title: "Gimnastyka Zdrowy Kręgosłup", sub: "Czwartki, Hala Wieliszew", tab: "pane-card" });
      recommendations.push({ title: "Bank Czasu Wieliszew", sub: "Saldo: 5.5 godz.", tab: "pane-timebank" });
    }

    botDiv.innerHTML = reply;
    recommendations.forEach(rec => {
      const recCard = document.createElement("div");
      recCard.className = "recommendation-card";
      recCard.innerHTML = `<strong>✨ ${rec.title}</strong><div>${rec.sub}</div>`;
      recCard.onclick = () => switchTab(rec.tab);
      botDiv.appendChild(recCard);
    });

    container.appendChild(botDiv);
    container.scrollTop = container.scrollHeight;

    // Speak reply if senior mode or voice preferred
    if (state.user.seniorMode) {
      speakText(reply);
    }
  }, 400);
}

// Leaflet Map Initialization
function initLeafletMap() {
  if (leafletMap) {
    leafletMap.invalidateSize();
    return;
  }

  const mapEl = document.getElementById("leaflet-map");
  if (!mapEl) return;

  // Center on Gmina Wieliszew (52.457°N, 20.978°E)
  leafletMap = L.map("leaflet-map").setView([52.457, 20.965], 12);

  L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    attribution: "© OpenStreetMap contributors | CUS Wieliszew",
    maxZoom: 18
  }).addTo(leafletMap);

  renderLeafletMarkers();
}

function renderLeafletMarkers() {
  if (!leafletMap) return;

  // Clear existing markers
  leafletMarkers.forEach(m => leafletMap.removeLayer(m));
  leafletMarkers = [];

  state.mapPins.forEach(pin => {
    const markerColor = pin.accepted ? "#2e7d32" : "#c62828";
    const customIcon = L.divIcon({
      className: "custom-map-pin",
      html: `<div style="background: ${markerColor}; color: #fff; width: 34px; height: 34px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 18px; border: 2px solid #fff; box-shadow: 0 3px 8px rgba(0,0,0,0.4);">${pin.icon}</div>`,
      iconSize: [34, 34],
      iconAnchor: [17, 17]
    });

    const m = L.marker([pin.lat, pin.lng], { icon: customIcon }).addTo(leafletMap);
    m.bindPopup(`
      <div style="font-family: sans-serif; font-size: 13px;">
        <strong style="color: #137547;">${pin.category} (${pin.village})</strong>
        <p style="margin: 4px 0;">${pin.title}</p>
        <span style="font-size: 11px; color: #666;">Nagroda: +${pin.points} pkt</span><br>
        <button style="background: #137547; color: #fff; border: none; padding: 4px 8px; border-radius: 6px; cursor: pointer; margin-top: 6px; font-weight: bold;" onclick="acceptNeedFromPopup('${pin.id}')">
          ${pin.accepted ? "✓ Podjęto" : "Chcę pomóc"}
        </button>
      </div>
    `);
    leafletMarkers.push(m);
  });
}

// Render Need Pins List below map
function renderNeedPins(filterVillage = "Wszystkie") {
  const list = document.getElementById("need-pins-list");
  if (!list) return;
  list.innerHTML = "";

  const filtered = state.mapPins.filter(p => filterVillage === "Wszystkie" || p.village === filterVillage);

  filtered.forEach(pin => {
    const card = document.createElement("div");
    card.className = "smart-card";
    card.innerHTML = `
      <div style="display: flex; justify-content: space-between; align-items: center;">
        <span class="badge ${pin.accepted ? 'badge-green' : 'badge-gold'}">
          ${pin.icon} ${pin.category} • ${pin.village}
        </span>
        <span class="badge ${pin.accepted ? 'badge-green' : 'badge-red'}">
          ${pin.accepted ? 'W REALIZACJI' : pin.urgency}
        </span>
      </div>
      <h4 style="font-size: 15px; margin: 8px 0 4px 0;">${pin.title}</h4>
      <p style="font-size: 12px; color: var(--primary); font-weight: 600;">📍 ${pin.address}</p>
      <p style="font-size: 13px; margin: 6px 0;">${pin.desc}</p>
      <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 10px;">
        <span style="font-weight: 800; color: #f57f17; font-size: 13px;">+${pin.points} pkt wolontariatu</span>
        <button class="btn ${pin.accepted ? 'btn-secondary' : 'btn-primary'}" style="width: auto; padding: 6px 14px;" onclick="acceptNeed('${pin.id}')" ${pin.accepted ? 'disabled' : ''}>
          ${pin.accepted ? '✓ Podjęto pomoc' : 'Chcę pomóc'}
        </button>
      </div>
    `;
    list.appendChild(card);
  });
}

function filterMapVillage(village, el) {
  document.querySelectorAll("#pane-map .chip").forEach(c => c.classList.remove("active"));
  if (el) el.classList.add("active");
  renderNeedPins(village);

  // Pan map
  if (leafletMap) {
    if (village === "Wieliszew") leafletMap.flyTo([52.457, 20.978], 14);
    else if (village === "Skrzeszew") leafletMap.flyTo([52.463, 20.925], 14);
    else if (village === "Michałów-Reginów") leafletMap.flyTo([52.433, 20.982], 14);
    else if (village === "Łajski") leafletMap.flyTo([52.428, 20.950], 14);
    else if (village === "Komornica") leafletMap.flyTo([52.470, 20.985], 14);
    else leafletMap.flyTo([52.457, 20.965], 12);
  }
}

function acceptNeed(id) {
  const pin = state.mapPins.find(p => p.id === id);
  if (!pin) return;
  pin.accepted = true;
  state.user.points += pin.points;

  // Update points display
  const pDisp = document.getElementById("gamification-points-display");
  if (pDisp) pDisp.textContent = `⭐ ${state.user.points} punktów aktywności`;

  renderNeedPins();
  renderLeafletMarkers();
  showToast(`🎉 Dziękujemy! Podjęto zadanie: "${pin.title}". Przyznano +${pin.points} pkt.`);
}

function acceptNeedFromPopup(id) {
  acceptNeed(id);
  if (leafletMap) leafletMap.closePopup();
}

// Modal: Add Need Pin
function openAddNeedModal() {
  document.getElementById("modal-add-need").classList.add("active");
}

function submitNewNeed() {
  const title = document.getElementById("new-need-title").value.trim();
  const desc = document.getElementById("new-need-desc").value.trim();
  const village = document.getElementById("new-need-village").value;

  if (!title) {
    alert("Proszę wpisać tytuł potrzeby.");
    return;
  }

  const newPin = {
    id: `pin-${Date.now()}`,
    title: title,
    category: "Pomoc sąsiedzka",
    icon: "🤝",
    urgency: "Pilne",
    village: village,
    address: `Rejon sołectwa ${village} (strefa zanonimizowana)`,
    desc: desc || "Potrzeba wsparcia sąsiedzkiego zgłoszona przez mieszkańca.",
    points: 40,
    lat: 52.450 + (Math.random() - 0.5) * 0.03,
    lng: 20.960 + (Math.random() - 0.5) * 0.05,
    accepted: false
  };

  state.mapPins.unshift(newPin);
  closeModals();
  renderNeedPins();
  renderLeafletMarkers();
  showToast("Dodano nowe zapotrzebowanie na Mapę Potrzeb.");
}

// Time Bank Flow
function renderTimeBank(filterType = "ALL") {
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
        <span style="font-weight: 800; color: var(--primary);">⏳ ${item.hours}h</span>
      </div>
      <h4 style="font-size: 15px; margin: 8px 0 2px 0;">${item.title}</h4>
      <p style="font-size: 12px; color: var(--text-muted);">${item.author} • ${item.rating}</p>
      <p style="font-size: 13px; margin: 6px 0;">${item.desc}</p>
      <button class="btn ${isOffer ? 'btn-primary' : 'btn-outline'}" style="margin-top: 8px;" onclick="exchangeTimeBank('${item.id}')">
        ${isOffer ? 'Wymień 1h ze swojego portfela' : 'Zaoferuj pomoc (Zarób 1h)'}
      </button>
    `;
    list.appendChild(card);
  });
}

function filterTimeBank(type, el) {
  document.querySelectorAll("#pane-timebank .chip").forEach(c => c.classList.remove("active"));
  if (el) el.classList.add("active");
  renderTimeBank(type);
}

function exchangeTimeBank(id) {
  const item = state.timeBank.find(i => i.id === id);
  if (!item) return;

  if (item.type === "OFFERING") {
    if (state.user.timeHours >= item.hours) {
      state.user.timeHours -= item.hours;
      showToast(`Wymieniono ${item.hours}h na usługę: "${item.title}". Pozostało: ${state.user.timeHours}h.`);
    } else {
      showToast("Nie masz wystarczającej liczby godzin w portfelu. Zaoferuj pomoc innemu sąsiadowi!");
    }
  } else {
    state.user.timeHours += item.hours;
    showToast(`Przyjęto zadanie! Po realizacji otrzymasz +${item.hours}h do portfela.`);
  }

  const bEl = document.getElementById("timebank-balance");
  if (bEl) bEl.textContent = `${state.user.timeHours} godz.`;
}

function openAddTimeBankModal() {
  document.getElementById("modal-add-timebank").classList.add("active");
}

function submitNewTimeBank() {
  const title = document.getElementById("new-tb-title").value.trim();
  const desc = document.getElementById("new-tb-desc").value.trim();
  const type = document.getElementById("new-tb-type").value;

  if (!title) {
    alert("Wpisz tytuł umiejętności.");
    return;
  }

  state.timeBank.unshift({
    id: `tb-${Date.now()}`,
    title: (type === "OFFERING" ? "Oferuję: " : "Poszukuję: ") + title,
    author: `${state.user.name} (${state.user.village})`,
    type: type,
    hours: 1.0,
    desc: desc || "Wymiana w ramach Banku Czasu Gminy Wieliszew.",
    rating: "⭐ Nowy"
  });

  closeModals();
  renderTimeBank();
  showToast("Opublikowano w Lokalnym Banku Czasu!");
}

// Gamification: Redeem rewards
function redeemReward(title, cost) {
  if (state.user.points >= cost) {
    state.user.points -= cost;
    const pDisp = document.getElementById("gamification-points-display");
    if (pDisp) pDisp.textContent = `⭐ ${state.user.points} punktów aktywności`;
    showToast(`🎉 Sukces! Odebrano voucher: "${title}". Kod rabatowy zapisano w Twojej e-Karcie.`);
  } else {
    showToast(`Brakuje Ci punktów! Wymagane: ${cost} pkt, masz: ${state.user.points} pkt.`);
  }
}

// Event registration
function toggleEventRegistration(id) {
  const idx = state.registeredEvents.indexOf(id);
  const statusEl = document.getElementById(`event-status-${id}`);
  const btnEl = document.getElementById(`btn-event-${id}`);

  if (idx > -1) {
    state.registeredEvents.splice(idx, 1);
    if (statusEl) {
      statusEl.className = "badge badge-gold";
      statusEl.textContent = "Wolne miejsca: 8";
    }
    if (btnEl) {
      btnEl.className = "btn btn-primary";
      btnEl.textContent = "Zapisz się z e-Kartą";
    }
    showToast("Zrezygnowano z zapisu na wydarzenie.");
  } else {
    state.registeredEvents.push(id);
    if (statusEl) {
      statusEl.className = "badge badge-green";
      statusEl.textContent = "✓ ZAPISANY";
    }
    if (btnEl) {
      btnEl.className = "btn btn-secondary";
      btnEl.textContent = "Wypisz się z zajęć";
    }
    showToast("Pomyślnie zapisano na zajęcia! Twoja wejściówka jest aktywna w e-Karcie.");
  }
}

// Telehealth: Video Consultation
function startVideoCall(name, role) {
  document.getElementById("video-specialist-name").textContent = name;
  document.getElementById("video-specialist-role").textContent = role;
  document.getElementById("video-main-label").textContent = name;
  document.getElementById("video-call-screen").classList.add("active");

  callDuration = 0;
  clearInterval(callInterval);
  callInterval = setInterval(() => {
    callDuration++;
    const mm = String(Math.floor(callDuration / 60)).padStart(2, "0");
    const ss = String(callDuration % 60).padStart(2, "0");
    document.getElementById("call-timer").textContent = `LIVE ${mm}:${ss}`;
  }, 1000);

  playTone(440, 0.3);
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
  document.getElementById("video-call-screen").classList.remove("active");
  showToast("Zakończono telekonsultację ze specjalistą CUS.");
}

// Sharing Hub Items
function renderSharingItems() {
  const cont = document.getElementById("sharing-items-container");
  if (!cont) return;
  cont.innerHTML = "";

  state.sharingItems.forEach(item => {
    const card = document.createElement("div");
    card.className = "smart-card";
    card.innerHTML = `
      <div style="display: flex; justify-content: space-between;">
        <span class="badge badge-green">${item.category}</span>
        <span class="badge ${item.reserved ? 'badge-gold' : 'badge-green'}">
          ${item.reserved ? 'ZAREZERWOWANY' : 'BEZPŁATNY'}
        </span>
      </div>
      <h4 style="font-size: 15px; margin: 8px 0 2px 0;">${item.title}</h4>
      <p style="font-size: 12px; color: var(--primary); font-weight: 600;">📍 ${item.location}</p>
      <p style="font-size: 12px; color: var(--text-muted);">${item.status}</p>
      <p style="font-size: 13px; margin: 6px 0;">${item.desc}</p>
      <button class="btn ${item.reserved ? 'btn-secondary' : 'btn-primary'}" style="margin-top: 8px;" onclick="toggleReserveSharing('${item.id}')">
        ${item.reserved ? '✓ Zarezerwowano (Kod: CUS-ODBIOR-491)' : 'Zarezerwuj do bezpłatnego odbioru'}
      </button>
    `;
    cont.appendChild(card);
  });
}

function toggleReserveSharing(id) {
  const item = state.sharingItems.find(i => i.id === id);
  if (!item) return;
  item.reserved = !item.reserved;
  renderSharingItems();
  showToast(
    item.reserved
      ? `Zarezerwowano: "${item.title}". Odbierz z kodem CUS-ODBIOR-491 w punkcie!`
      : "Anulowano rezerwację."
  );
}

// Modals close
function closeModals() {
  document.querySelectorAll(".modal-overlay").forEach(m => m.classList.remove("active"));
}

