## 🏛️ Smart CUS Wieliszew v1.1.0 – GovTech 2.0 (Material Design 3)

Oficjalna, zaktualizowana wersja produkcyjna aplikacji mobilnej dla mieszkańców oraz Centrum Usług Społecznych w Wieliszewie, zrealizowana w **100% w natywnym języku Kotlin (Jetpack Compose)**, ściśle według wytycznych **Material Design 3 (Material You)** oraz **WCAG 2.1 AAA**.

---

### 📦 Pliki do Pobrania (Assets):
* 📱 **[`SmartCUS_Wieliszew.apk`](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/download/v1.1.0/SmartCUS_Wieliszew.apk)** – Główny pakiet instalacyjny Android APK (kompatybilny z Androidem 8.0 lub nowszym).
* 📱 **[`SmartCUS_Wieliszew_v1.1.0.apk`](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/download/v1.1.0/SmartCUS_Wieliszew_v1.1.0.apk)** – Wersjonowany pakiet instalacyjny wydania v1.1.0.
* 📦 **[`SmartCUS_Wieliszew_Source.zip`](https://github.com/jakub-jatkowski/smart-cus-wieliszew/releases/download/v1.1.0/SmartCUS_Wieliszew_Source.zip)** – Kompletne archiwum kodów źródłowych Kotlin / Gradle projektu.

---

### 🌟 Najważniejsze Zmiany i Usprawnienia w Wersji 1.1.0:

1. **💳 Karta Mieszkańca (WKM) 3D Flip & TOTP QR Code:**
   - Płynny trójwymiarowy obrót karty w osi Y (`rotationY`) prezentujący awers oraz rewers z danymi kontaktowymi ICE, kodem kreskowym i infolinią CUS.
   - Prawdziwy generator kodów QR (ZXing) z dynamicznym odliczaniem tokenu TOTP (45 sekund) i pełnoekranowym powiększeniem do skanowania w obiektach OSiR / GCK.
   - Zintegrowany portfel voucherów i zniżek mieszkańca z obsługą schowka oraz symulator weryfikacji zbliżeniowej NFC.

2. **🤖 Wirtualny Doradca AI & Polski Voicebot:**
   - Wizualizacja fal dźwiękowych mowy (`VoiceWaveformBar`) z natychmiastowym rozpoznawaniem i syntezą mowy (TTS).
   - Zaktualizowana baza wiedzy o kryteriach dochodowych pomocy społecznej na 2026 r. (1 010 zł samotni / 776 zł rodzina), regulaminie Jadłodzielni i procedurach CUS.

3. **📍 Interaktywna Mapa Potrzeb i Mikrowolontariatu:**
   - Geograficzny radar sołectw Gminy Wieliszew i Zalewu Zegrzyńskiego z 200-metrowym buforem rozmycia RODO.
   - W pełni funkcjonalny modal zgłaszania nowej potrzeby oraz przyjmowania i rozliczania zadań sąsiedzkich.

4. **⏳ Lokalny Bank Czasu (1h za 1h):**
   - Natywny portfel godzinowy mieszkańca z ewidencją transakcji czasowych.
   - Formularz dodawania ofert i poszukiwanych umiejętności (naprawy, transport, korepetycje, ogród).

5. **🍏 Magazyn Współdzielenia & Gminna Jadłodzielnia 24/7:**
   - Karta całodobowej Jadłodzielni przy ul. Modlińskiej 65 z podglądem artykułów i opcją „Przekaż dar”.
   - Wypożyczalnia sprzętu rehabilitacyjnego (wózki, balkoniki) z automatycznym generowaniem unikalnych kodów odbioru w CUS.

6. **🩺 Teleopieka (WebRTC) & Konsultacje:**
   - Bezpieczne, szyfrowane konsultacje wideo z psychologiem, radcą prawnym i koordynatorem opieki.
   - Podgląd własnej kamery PIP, stoper trwania połączenia na żywo oraz terminarz rezerwacji wizyt.

7. **🏆 Grywalizacja & Nagrody Społeczne:**
   - System 4 poziomów zaangażowania mieszkańca z paskiem postępu.
   - Interaktywne odznaki („Serce Gminy”, „Aktywny Sąsiad”, „Mistrz Eko”) i wymiana punktów na realne vouchery gminne.

8. **🎨 Design System Material You & Dostępność WCAG 2.1 AAA:**
   - 3 motywy kolorystyczne: Wieliszewska Zieleń 🌿, Słoneczny Bursztyn ☀️, Błękit Zalewu Zegrzyńskiego 🌊.
   - Dedykowany tryb wysokiego kontrastu dla seniorów (WCAG 2.1 AAA) ze współczynnikiem kontrastu powyżej 7:1.
   - Bezpieczne marginesy dolne (`contentPadding = 96.dp`) eliminujące nakładanie się elementów pod paskiem nawigacji.
