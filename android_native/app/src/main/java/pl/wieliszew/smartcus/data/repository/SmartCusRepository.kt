package pl.wieliszew.smartcus.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import pl.wieliszew.smartcus.data.model.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SmartCusRepository {

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _residentCard = MutableStateFlow(ResidentCard())
    val residentCard: StateFlow<ResidentCard> = _residentCard.asStateFlow()

    private val _events = MutableStateFlow(initialEvents())
    val events: StateFlow<List<CusEvent>> = _events.asStateFlow()

    private val _needPins = MutableStateFlow(initialNeedPins())
    val needPins: StateFlow<List<NeedPin>> = _needPins.asStateFlow()

    private val _timeBankEntries = MutableStateFlow(initialTimeBankEntries())
    val timeBankEntries: StateFlow<List<TimeBankEntry>> = _timeBankEntries.asStateFlow()

    private val _badges = MutableStateFlow(initialBadges())
    val badges: StateFlow<List<Badge>> = _badges.asStateFlow()

    private val _rewards = MutableStateFlow(initialRewards())
    val rewards: StateFlow<List<GamificationReward>> = _rewards.asStateFlow()

    private val _specialists = MutableStateFlow(initialSpecialists())
    val specialists: StateFlow<List<TelehealthSpecialist>> = _specialists.asStateFlow()

    private val _sharedItems = MutableStateFlow(initialSharedItems())
    val sharedItems: StateFlow<List<SharedItem>> = _sharedItems.asStateFlow()

    private val _chatMessages = MutableStateFlow(initialChat())
    val chatMessages: StateFlow<List<AiMessage>> = _chatMessages.asStateFlow()

    private val _isSeniorMode = MutableStateFlow(false)
    val isSeniorMode: StateFlow<Boolean> = _isSeniorMode.asStateFlow()

    fun toggleSeniorMode() {
        _isSeniorMode.update { !it }
    }

    fun toggleEventRegistration(eventId: String) {
        _events.update { list ->
            list.map { event ->
                if (event.id == eventId) {
                    val newRegistered = !event.isRegistered
                    val delta = if (newRegistered) 1 else -1
                    event.copy(
                        isRegistered = newRegistered,
                        spotsTaken = (event.spotsTaken + delta).coerceIn(0, event.spotsTotal)
                    )
                } else event
            }
        }
    }

    fun acceptNeedPin(pinId: String) {
        _needPins.update { list ->
            list.map { pin ->
                if (pin.id == pinId) {
                    pin.copy(
                        status = NeedStatus.IN_PROGRESS,
                        acceptedByVolunteer = true
                    )
                } else pin
            }
        }
        // Grant volunteer points
        _userProfile.update { it.copy(gamificationPoints = it.gamificationPoints + 50) }
    }

    fun addNeedPin(title: String, category: NeedCategory, village: String, description: String, urgency: Urgency) {
        val newPin = NeedPin(
            id = "need_${System.currentTimeMillis()}",
            title = title,
            category = category,
            urgency = urgency,
            village = village,
            approximateAddress = "Rejon sołectwa $village (strefa anonimizowana)",
            description = description,
            status = NeedStatus.OPEN,
            pointsReward = 40
        )
        _needPins.update { listOf(newPin) + it }
    }

    fun addTimeBankEntry(title: String, village: String, type: TimeBankType, hours: Double, category: String, desc: String) {
        val entry = TimeBankEntry(
            id = "tb_${System.currentTimeMillis()}",
            title = title,
            authorName = _userProfile.value.fullName,
            village = village,
            type = type,
            hours = hours,
            category = category,
            description = desc
        )
        _timeBankEntries.update { listOf(entry) + it }
    }

    fun transferTimeBankHours(entryId: String, hours: Double) {
        _userProfile.update { current ->
            current.copy(timeBankBalanceHours = (current.timeBankBalanceHours + hours).coerceAtLeast(0.0))
        }
    }

    fun redeemReward(rewardId: String): Boolean {
        val reward = _rewards.value.find { it.id == rewardId } ?: return false
        if (_userProfile.value.gamificationPoints >= reward.pointsCost) {
            _userProfile.update { it.copy(gamificationPoints = it.gamificationPoints - reward.pointsCost) }
            return true
        }
        return false
    }

    fun reserveSharedItem(itemId: String) {
        _sharedItems.update { list ->
            list.map { item ->
                if (item.id == itemId) {
                    item.copy(isReserved = !item.isReserved)
                } else item
            }
        }
    }

    fun sendUserMessage(question: String) {
        val timeNow = "Teraz"
        val userMsg = AiMessage(
            id = "msg_${System.currentTimeMillis()}",
            sender = MessageSender.USER,
            text = question,
            timestamp = timeNow
        )
        _chatMessages.update { it + userMsg }

        // Smart reply simulation based on keywords
        val lower = question.lowercase()
        val botResponse = when {
            "karta" in lower || "wkm" in lower || "kod qr" in lower -> {
                AiMessage(
                    id = "bot_${System.currentTimeMillis()}",
                    sender = MessageSender.ASSISTANT,
                    text = "Twoja cyfrowa e-Wieliszewska Karta Mieszkańca jest aktywna w zakładce 'e-Karta'. Kod QR odświeża się automatycznie co 45 sekund, co chroni Twoje uprawnienia. Możesz też przyłożyć telefon do czytnika NFC!",
                    timestamp = timeNow,
                    recommendations = listOf(
                        Recommendation("r1", "Pokaż moją e-Kartę", "Weryfikacja wejść i zniżek", "card", "Dostępne od ręki")
                    )
                )
            }
            "sprzęt" in lower || "wózek" in lower || "rehabilitac" in lower -> {
                AiMessage(
                    id = "bot_${System.currentTimeMillis()}",
                    sender = MessageSender.ASSISTANT,
                    text = "W magazynie cyrkularnym CUS Wieliszew mamy bezpłatnie dostępny sprzęt rehabilitacyjny (np. wózek inwalidzki lekki oraz balkonik czterokołowy). Możesz zarezerwować go jednym kliknięciem w module 'Współdzielenie'.",
                    timestamp = timeNow,
                    recommendations = listOf(
                        Recommendation("r2", "Zarezerwuj sprzęt", "Odbiór w CUS przy ul. Modlińskiej", "sharing", "Bezpłatne wypożyczenie")
                    )
                )
            }
            "zakupy" in lower || "pomoc" in lower || "wolontariusz" in lower -> {
                AiMessage(
                    id = "bot_${System.currentTimeMillis()}",
                    sender = MessageSender.ASSISTANT,
                    text = "Dla mieszkańców potrzebujących wsparcia działa nasza Interaktywna Mapa Potrzeb. Znajdziesz tam zgłoszenia sąsiedzkie lub możesz dodać własną prośbę o pomoc w zakupach czy drobnej naprawie.",
                    timestamp = timeNow,
                    recommendations = listOf(
                        Recommendation("r3", "Otwórz Mapę Potrzeb", "Sprawdź 4 zgłoszenia w Wieliszewie i Skrzeszewie", "map", "Mikrowolontariat")
                    )
                )
            }
            "psycholog" in lower || "prawnik" in lower || "rozmowa" in lower -> {
                AiMessage(
                    id = "bot_${System.currentTimeMillis()}",
                    sender = MessageSender.ASSISTANT,
                    text = "W CUS Wieliszew oferujemy bezpłatne i poufne telekonsultacje e-wsparcia. Dzisiaj wolny termin ma psycholog mgr Anna Wiśniewska oraz radca prawny mec. Piotr Dąbrowski.",
                    timestamp = timeNow,
                    recommendations = listOf(
                        Recommendation("r4", "Połącz ze specjalistą", "Szyfrowana wideorozmowa bez wychodzenia z domu", "telehealth", "Konsultacja bezpłatna")
                    )
                )
            }
            else -> {
                AiMessage(
                    id = "bot_${System.currentTimeMillis()}",
                    sender = MessageSender.ASSISTANT,
                    text = "Witaj w Smart CUS Wieliszew! Mogę pomóc Ci w zapisaniu się na zajęcia prozdrowotne (np. 'Zdrowy Kręgosłup dla Seniorów'), znalezieniu wolontariusza na Mapie Potrzeb, wymianie w Banku Czasu lub rezerwacji sprzętu z magazynu.",
                    timestamp = timeNow,
                    recommendations = listOf(
                        Recommendation("r5", "Zdrowy Kręgosłup 60+", "Zajęcia gimnastyczne w Hali w Wieliszewie", "events", "Rekomendowane dla Ciebie"),
                        Recommendation("r6", "Lokalny Bank Czasu", "Wymień pomoc na punkty czasowe", "timebank", "Wspólnota gminna")
                    )
                )
            }
        }
        _chatMessages.update { it + botResponse }
    }

    private companion object {
        fun initialEvents() = listOf(
            CusEvent(
                id = "ev_01",
                title = "Gimnastyka 'Zdrowy Kręgosłup 60+'",
                category = "Profilaktyka Zdrowia",
                dateText = "Czwartki, godz. 10:00 (najbliższe: 5 Listopada)",
                location = "Hala Sportowa, ul. Modlińska 65, Wieliszew",
                description = "Łagodne ćwiczenia rozciągające i wzmacniające pod okiem fizjoterapeuty CUS. Darmowy wstęp z e-Kartą Mieszkańca.",
                spotsTotal = 25,
                spotsTaken = 19,
                isRegistered = true
            ),
            CusEvent(
                id = "ev_02",
                title = "Cyfrowe Czwartki: Smartfon bez tajemnic dla seniora",
                category = "Edukacja Cyfrowa",
                dateText = "12 Listopada 2026, godz. 11:30",
                location = "Gminna Biblioteka Publiczna w Skrzeszewie",
                description = "Praktyczne warsztaty: bezpieczne płatności, obsługa e-Karty Wieliszewskiej i telekonsultacji z lekarzem.",
                spotsTotal = 15,
                spotsTaken = 8,
                isRegistered = false
            ),
            CusEvent(
                id = "ev_03",
                title = "Sąsiedzkie Gotowanie i Wymiana Przepisów CUS",
                category = "Integracja Międzypokoleniowa",
                dateText = "18 Listopada 2026, godz. 16:00",
                location = "Świetlica Wiejska w Michałowie-Reginowie",
                description = "Wspólne przygotowywanie jesiennych przetworów i dań regionalnych. Nadwyżki trafią do gminnej Jadłodzielni.",
                spotsTotal = 20,
                spotsTaken = 14,
                isRegistered = false
            )
        )

        fun initialNeedPins() = listOf(
            NeedPin(
                id = "need_01",
                title = "Wniesienie zgrzewki wody i leków z apteki",
                category = NeedCategory.SHOPPING,
                urgency = Urgency.TODAY,
                village = "Wieliszew",
                approximateAddress = "Okolice ul. Kościelnej i Parku Świętojańskiego",
                description = "Pani Maria (82 l.) po zabiegu kolana potrzebuje pomocy w odbiorze zamówionych leków z apteki oraz 2 zgrzewek wody. Leki są już opłacone.",
                status = NeedStatus.OPEN,
                pointsReward = 50
            ),
            NeedPin(
                id = "need_02",
                title = "Podwiezienie do przychodni w Legionowie",
                category = NeedCategory.TRANSPORT,
                urgency = Urgency.THIS_WEEK,
                village = "Skrzeszew",
                approximateAddress = "Rejon ul. Polnej / Partyzantów",
                description = "Pan Stanisław potrzebuje transportu na badanie USG we wtorek o 9:30 z powrotem do Skrzeszewa. Porusza się o lasce.",
                status = NeedStatus.OPEN,
                pointsReward = 80
            ),
            NeedPin(
                id = "need_03",
                title = "Pomoc w konfiguracji nowego dekodera TV",
                category = NeedCategory.REPAIR,
                urgency = Urgency.FLEXIBLE,
                village = "Michałów-Reginów",
                approximateAddress = "Okolice stacji PKP Michałów-Reginów",
                description = "Prosta pomoc w podłączeniu kabla HDMI i przeskanowaniu programów w telewizorze dla starszego małżeństwa.",
                status = NeedStatus.OPEN,
                pointsReward = 45
            ),
            NeedPin(
                id = "need_04",
                title = "Wyprowadzenie psa rasy Beagle po południu",
                category = NeedCategory.PET_CARE,
                urgency = Urgency.TODAY,
                village = "Łajski",
                approximateAddress = "Rejon ul. Sikorskiego",
                description = "Krótki 30-minutowy spacer z łagodnym psem. Właścicielka tymczasowo unieruchomiona w łóżku z grypą.",
                status = NeedStatus.OPEN,
                pointsReward = 35
            )
        )

        fun initialTimeBankEntries() = listOf(
            TimeBankEntry(
                id = "tb_01",
                title = "Oferuję: Pomoc w drobnych naprawach domowych i hydraulice",
                authorName = "Tadeusz N.",
                village = "Wieliszew",
                type = TimeBankType.OFFERING,
                hours = 1.0,
                category = "Złota rączka",
                description = "Wymiana uszczelki w kranie, odpowietrzenie kaloryfera, regulacja drzwi. Działam na terenie Wieliszewa i Komornicy.",
                rating = 5.0
            ),
            TimeBankEntry(
                id = "tb_02",
                title = "Oferuję: Korepetycje z języka angielskiego dla młodzieży / seniorów",
                authorName = "Magdalena K.",
                village = "Michałów-Reginów",
                type = TimeBankType.OFFERING,
                hours = 1.0,
                category = "Edukacja",
                description = "Konwersacje od podstaw, ćwiczenie słownictwa na wyjazdy lub pomoc w lekcjach szkolnych.",
                rating = 4.9
            ),
            TimeBankEntry(
                id = "tb_03",
                title = "Poszukuję: Pomoc w zgrabieniu liści i przygotowaniu ogrodu do zimy",
                authorName = "Helena W.",
                village = "Skrzeszew",
                type = TimeBankType.REQUESTING,
                hours = 2.0,
                category = "Ogród",
                description = "Niewielki ogródek przydomowy, mam własne grabie i worki kompostowe. Zapewniam herbatę i domowe ciasto drożdżowe!",
                rating = 4.8
            )
        )

        fun initialBadges() = listOf(
            Badge("b1", "Serce Wieliszewa", "Zrealizowano min. 5 zadań mikrowolontariatu na Mapie Potrzeb", "❤️", true, "15.10.2026"),
            Badge("b2", "Aktywny Sąsiad", "Pierwsza udana wymiana w Lokalnym Banku Czasu", "🤝", true, "22.10.2026"),
            Badge("b3", "Mistrz Ekologii CUS", "Skorzystano z Magazynu Współdzielenia lub Jadłodzielni", "🌱", true, "28.10.2026"),
            Badge("b4", "Promotor Zdrowia", "Udział w 3 zajęciach profilaktycznych organizowanych przez CUS", "🏅", false),
            Badge("b5", "Złoty Wolontariusz 2026", "Ponad 1000 punktów zaangażowania społecznego", "🏆", false)
        )

        fun initialRewards() = listOf(
            GamificationReward(
                id = "rew_01",
                title = "Karnet 1h: Kajak lub Rower Wodny nad Narwią / Zegrzem",
                partnerName = "Przystań Wieliszew & OSiR",
                pointsCost = 250,
                discountPercent = 100,
                description = "Bezpłatna godzina wypożyczenia sprzętu wodnego w sezonie letnim.",
                category = "Rekreacja i Sport"
            ),
            GamificationReward(
                id = "rew_02",
                title = "Zniżka 30% na bilet teatralny / koncert",
                partnerName = "Gminne Centrum Kultury w Łajskach",
                pointsCost = 150,
                discountPercent = 30,
                description = "Ważny na dowolne wydarzenie biletowane organizowane przez gminę.",
                category = "Kultura"
            ),
            GamificationReward(
                id = "rew_03",
                title = "Darmowa kawa i ciastko sąsiedzkie",
                partnerName = "Lokalna Piekarnia i Kawiarnia 'Słoneczna'",
                pointsCost = 100,
                discountPercent = 100,
                description = "Do odebrania przy okazaniu kodu QR z e-Karty.",
                category = "Gastronomia Lokalna"
            )
        )

        fun initialSpecialists() = listOf(
            TelehealthSpecialist(
                id = "spec_01",
                name = "mgr Anna Wiśniewska",
                type = SpecialistType.PSYCHOLOGIST,
                availableToday = true,
                nextFreeSlot = "Dzisiaj, godz. 13:30",
                bio = "Psycholog z wieloletnim doświadczeniem w pracy z seniorami, wsparciu w kryzysie i żałobie. Dyżur CUS."
            ),
            TelehealthSpecialist(
                id = "spec_02",
                name = "mec. Piotr Dąbrowski",
                type = SpecialistType.LAWYER,
                availableToday = true,
                nextFreeSlot = "Dzisiaj, godz. 15:00",
                bio = "Nieodpłatna pomoc prawna: sprawy majątkowe, spadkowe, prawa lokatorskie i uprawnienia socjalne."
            ),
            TelehealthSpecialist(
                id = "spec_03",
                name = "mgr Elżbieta Zielińska",
                type = SpecialistType.SOCIAL_WORKER,
                availableToday = false,
                nextFreeSlot = "Jutro, godz. 09:00",
                bio = "Koordynator Indywidualnych Planów Usług Społecznych w CUS Wieliszew. Doradztwo opiekuńcze."
            )
        )

        fun initialSharedItems() = listOf(
            SharedItem(
                id = "sh_01",
                title = "Wózek inwalidzki lekki składany",
                category = ShareCategory.REHAB_EQUIPMENT,
                pickupLocation = "Punkt CUS, ul. Modlińska 65, Wieliszew",
                availableCount = 2,
                condition = "Stan bardzo dobry, po dezynfekcji i przeglądzie technicznym",
                description = "Lekki, aluminiowy wózek dla osoby dorosłej. Maksymalne obciążenie 120 kg. Bezpłatne wypożyczenie do 6 miesięcy.",
                isReserved = false
            ),
            SharedItem(
                id = "sh_02",
                title = "Balkonik czterokołowy z koszykiem i hamulcem",
                category = ShareCategory.REHAB_EQUIPMENT,
                pickupLocation = "Świetlica w Skrzeszewie",
                availableCount = 1,
                condition = "Idealny",
                description = "Ułatwia codzienne spacery i zakupy. Składany, z wygodnym siedziskiem.",
                isReserved = false
            ),
            SharedItem(
                id = "sh_03",
                title = "Świeże jabłka i przetwory z lokalnego sadu (Jadłodzielnia)",
                category = ShareCategory.FOOD_SHARING,
                pickupLocation = "Jadłodzielnia Wieliszew (obok CUS)",
                availableCount = 8,
                condition = "Świeże, zebrane wczoraj",
                description = "Nadwyżka od lokalnego sadownika z Kałuszyna. Zapraszamy do częstowania się!",
                isReserved = false
            ),
            SharedItem(
                id = "sh_04",
                title = "Kule łokciowe z regulacją wysokości (para)",
                category = ShareCategory.REHAB_EQUIPMENT,
                pickupLocation = "Punkt CUS, ul. Modlińska 65",
                availableCount = 3,
                condition = "Bardzo dobry",
                description = "Anatomiczne uchwyty, odblaski, szybka regulacja wysokości.",
                isReserved = false
            )
        )

        fun initialChat() = listOf(
            AiMessage(
                id = "bot_init",
                sender = MessageSender.ASSISTANT,
                text = "Dzień dobry Pani Janino! Jestem Wirtualnym Asystentem Smart CUS Wieliszew. W czym mogę Pani dziś pomóc? Mogę np. pokazać Pani e-Kartę, sprawdzić wolne terminy gimnastyki dla seniorów lub połączyć z psychologiem.",
                timestamp = "10:00",
                recommendations = listOf(
                    Recommendation("rec_1", "Zajęcia Zdrowy Kręgosłup", "W najbliższy czwartek w Hali w Wieliszewie", "events", "Dla Ciebie"),
                    Recommendation("rec_2", "Pokaż e-Kartę Mieszkańca", "Szybki kod QR z uprawnieniami", "card", "e-Portfel")
                )
            )
        )
    }
}

