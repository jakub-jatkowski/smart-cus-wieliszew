package pl.wieliszew.smartcus.data.repository

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.wieliszew.smartcus.data.model.*
import pl.wieliszew.smartcus.data.remote.BackendConfig
import pl.wieliszew.smartcus.data.remote.CusApiService
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SmartCusRepository(
    private val context: Context? = null
) {
    private val prefs = context?.getSharedPreferences("smart_cus_offline_prefs", Context.MODE_PRIVATE)

    val apiService = CusApiService()
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val _isBackendOnline = MutableStateFlow(false)
    val isBackendOnline: StateFlow<Boolean> = _isBackendOnline.asStateFlow()

    private val _serverUrl = MutableStateFlow(
        prefs?.getString("server_url", BackendConfig.baseUrl) ?: BackendConfig.baseUrl
    )
    val serverUrl: StateFlow<String> = _serverUrl.asStateFlow()

    private val _userProfile = MutableStateFlow(
        UserProfile(
            fullName = prefs?.getString("user_name", "Janina Kowalska") ?: "Janina Kowalska",
            village = prefs?.getString("user_village", "Wieliszew") ?: "Wieliszew",
            iceContact = prefs?.getString("ice_contact", "Córka Anna: +48 501 234 567") ?: "Córka Anna: +48 501 234 567",
            gamificationPoints = prefs?.getInt("gamification_points", 420) ?: 420,
            timeBankBalanceHours = (prefs?.getFloat("timebank_hours", 5.5f) ?: 5.5f).toDouble()
        )
    )
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _residentCard = MutableStateFlow(
        ResidentCard(
            holderName = prefs?.getString("user_name", "Janina Kowalska") ?: "Janina Kowalska"
        )
    )
    val residentCard: StateFlow<ResidentCard> = _residentCard.asStateFlow()

    private val _vouchers = MutableStateFlow<List<Voucher>>(emptyList())
    val vouchers: StateFlow<List<Voucher>> = _vouchers.asStateFlow()

    private val _notifications = MutableStateFlow(initialNotifications())
    val notifications: StateFlow<List<MunicipalNotification>> = _notifications.asStateFlow()

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

    private val _isSeniorMode = MutableStateFlow(prefs?.getBoolean("senior_mode", false) ?: false)
    val isSeniorMode: StateFlow<Boolean> = _isSeniorMode.asStateFlow()

    init {
        scope.launch {
            _userProfile.collect { p ->
                prefs?.edit()
                    ?.putString("user_name", p.fullName)
                    ?.putString("user_village", p.village)
                    ?.putString("ice_contact", p.iceContact)
                    ?.putInt("gamification_points", p.gamificationPoints)
                    ?.putFloat("timebank_hours", p.timeBankBalanceHours.toFloat())
                    ?.apply()
            }
        }
        syncWithBackend()
    }

    fun updateServerUrl(newUrl: String) {
        BackendConfig.baseUrl = newUrl
        _serverUrl.value = newUrl
        syncWithBackend()
    }

    fun syncWithBackend() {
        scope.launch {
            val online = apiService.checkHealth()
            _isBackendOnline.value = online
            if (online) {
                // Fetch and apply resident data from backend
                apiService.getResident()?.let { r ->
                    val name = r["full_name"] as? String ?: return@let
                    val vill = r["village"] as? String ?: "Wieliszew"
                    val ice = r["ice_contact"] as? String ?: ""
                    val points = (r["points"] as? Number)?.toInt() ?: 420
                    val timeBal = (r["time_balance"] as? Number)?.toDouble() ?: 5.5
                    _userProfile.update {
                        it.copy(
                            fullName = name,
                            village = vill,
                            iceContact = ice,
                            gamificationPoints = points,
                            timeBankBalanceHours = timeBal
                        )
                    }
                    _residentCard.update { it.copy(holderName = name) }
                }

                // Fetch and apply need pins from backend
                apiService.getNeedPins()?.let { remoteList ->
                    if (remoteList.isNotEmpty()) {
                        val mapped = remoteList.map { m ->
                            NeedPin(
                                id = m["id"] as? String ?: "need_${System.currentTimeMillis()}",
                                title = m["title"] as? String ?: "",
                                category = when (m["category"] as? String) {
                                    "SHOPPING" -> NeedCategory.SHOPPING
                                    "TRANSPORT" -> NeedCategory.TRANSPORT
                                    "REPAIR" -> NeedCategory.REPAIR
                                    "COMPANIONSHIP" -> NeedCategory.COMPANIONSHIP
                                    "PET_CARE", "PETS" -> NeedCategory.PET_CARE
                                    "GARDEN", "GARDENING" -> NeedCategory.GARDEN
                                    else -> NeedCategory.SHOPPING
                                },
                                urgency = when (m["urgency"] as? String) {
                                    "TODAY", "HIGH" -> Urgency.TODAY
                                    "THIS_WEEK", "MEDIUM" -> Urgency.THIS_WEEK
                                    else -> Urgency.FLEXIBLE
                                },
                                village = m["village"] as? String ?: "Wieliszew",
                                approximateAddress = "Rejon sołectwa ${m["village"] as? String ?: "Wieliszew"} (bufor RODO 200m)",
                                description = m["description"] as? String ?: "",
                                status = when (m["status"] as? String) {
                                    "IN_PROGRESS" -> NeedStatus.IN_PROGRESS
                                    "COMPLETED" -> NeedStatus.COMPLETED
                                    else -> NeedStatus.OPEN
                                },
                                pointsReward = (m["points"] as? Number)?.toInt() ?: 40,
                                latitude = (m["lat"] as? Number)?.toDouble() ?: getVillageCoordinates(m["village"] as? String ?: "Wieliszew").first,
                                longitude = (m["lng"] as? Number)?.toDouble() ?: getVillageCoordinates(m["village"] as? String ?: "Wieliszew").second
                            )
                        }
                        _needPins.value = mapped
                    }
                }
            }
        }
    }

    fun toggleSeniorMode() {
        _isSeniorMode.update {
            val newVal = !it
            prefs?.edit()?.putBoolean("senior_mode", newVal)?.apply()
            newVal
        }
    }

    fun updateUserProfile(fullName: String, village: String, iceContact: String) {
        _userProfile.update { it.copy(fullName = fullName, village = village, iceContact = iceContact) }
        _residentCard.update { it.copy(holderName = fullName) }
        prefs?.edit()
            ?.putString("user_name", fullName)
            ?.putString("user_village", village)
            ?.putString("ice_contact", iceContact)
            ?.apply()
    }

    fun clearChat() {
        _chatMessages.value = initialChat()
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
        _userProfile.update { it.copy(gamificationPoints = it.gamificationPoints + 50) }
        scope.launch {
            apiService.acceptNeedPin(pinId)
        }
    }

    fun completeNeedPin(pinId: String) {
        _needPins.update { list ->
            list.map { pin ->
                if (pin.id == pinId) {
                    pin.copy(status = NeedStatus.COMPLETED)
                } else pin
            }
        }
        scope.launch {
            apiService.completeNeedPin(pinId)
        }
    }

    fun addNeedPin(title: String, category: NeedCategory, village: String, description: String, urgency: Urgency) {
        val (vLat, vLng) = getVillageCoordinates(village)
        val jitterLat = vLat + (Math.random() - 0.5) * 0.005
        val jitterLng = vLng + (Math.random() - 0.5) * 0.005
        val newPin = NeedPin(
            id = "need_${System.currentTimeMillis()}",
            title = title,
            category = category,
            urgency = urgency,
            village = village,
            approximateAddress = "Rejon sołectwa $village (strefa RODO 200m)",
            description = description,
            status = NeedStatus.OPEN,
            pointsReward = 40,
            latitude = jitterLat,
            longitude = jitterLng
        )
        _needPins.update { listOf(newPin) + it }
        scope.launch {
            apiService.createNeedPin(title, description, village, category.name, urgency.name)
        }
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
        scope.launch {
            apiService.createTimeBankEntry(_userProfile.value.fullName, village, type.name, category, title, desc, hours)
        }
    }

    fun transferTimeBankHours(entryId: String, hours: Double) {
        _userProfile.update { current ->
            current.copy(timeBankBalanceHours = (current.timeBankBalanceHours + hours).coerceAtLeast(0.0))
        }
        scope.launch {
            apiService.exchangeTimeBank(entryId)
        }
    }

    fun redeemReward(rewardId: String): Boolean {
        val reward = _rewards.value.find { it.id == rewardId } ?: return false
        if (_userProfile.value.gamificationPoints >= reward.pointsCost) {
            _userProfile.update { it.copy(gamificationPoints = it.gamificationPoints - reward.pointsCost) }
            val newVoucher = Voucher(
                id = "vouch_${System.currentTimeMillis()}",
                title = reward.title,
                partnerName = reward.partnerName,
                code = "WKM-${(1000..9999).random()}-${System.currentTimeMillis() % 10000}"
            )
            _vouchers.update { listOf(newVoucher) + it }
            return true
        }
        return false
    }

    fun reserveSharedItem(itemId: String) {
        _sharedItems.update { list ->
            list.map { item ->
                if (item.id == itemId) {
                    val newReserved = !item.isReserved
                    item.copy(
                        isReserved = newReserved,
                        reservationCode = if (newReserved) "CUS-ODBIOR-${(100..999).random()}" else null
                    )
                } else item
            }
        }
        scope.launch {
            apiService.reserveEquipment(itemId)
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

        scope.launch {
            if (_isBackendOnline.value) {
                val remoteAnswer = apiService.askAssistant(question)
                if (remoteAnswer != null) {
                    _chatMessages.update {
                        it + AiMessage(
                            id = "bot_${System.currentTimeMillis()}",
                            sender = MessageSender.ASSISTANT,
                            text = remoteAnswer,
                            timestamp = timeNow,
                            recommendations = emptyList()
                        )
                    }
                    return@launch
                }
            }

            val lower = question.lowercase()
            val botResponse = when {
            "karta" in lower || "wkm" in lower || "kod qr" in lower || "wyrobić" in lower -> {
                AiMessage(
                    id = "bot_${System.currentTimeMillis()}",
                    sender = MessageSender.ASSISTANT,
                    text = "Twoja cyfrowa e-Wieliszewska Karta Mieszkańca (WKM) jest aktywna w zakładce 'e-Karta'. Kod QR odświeża się automatycznie co 45 sekund, co chroni Twoje uprawnienia. Możesz też przyłożyć telefon do czytnika NFC!",
                    timestamp = timeNow,
                    recommendations = listOf(
                        Recommendation("r1", "Pokaż moją e-Kartę", "Weryfikacja wejść i zniżek", "card", "Dostępne od ręki"),
                        Recommendation("r1b", "Moje Vouchery", "Zniżki i nagrody mieszkańca", "card", "e-Portfel")
                    )
                )
            }
            "kryter" in lower || "dochod" in lower || "zasił" in lower -> {
                AiMessage(
                    id = "bot_${System.currentTimeMillis()}",
                    sender = MessageSender.ASSISTANT,
                    text = "W 2026 r. kryterium dochodowe w pomocy społecznej wynosi 1 010 zł dla osoby samotnie gospodarującej oraz 776 zł na osobę w rodzinie. CUS Wieliszew przyznaje zasiłki stałe, okresowe i celowe (np. na opał, leki).",
                    timestamp = timeNow,
                    recommendations = listOf(
                        Recommendation("r_soc", "Dyżur Koordynatora CUS", "Zasiłki i wsparcie socjalne", "telehealth", "Konsultacja")
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
                        Recommendation("r3", "Otwórz Mapę Potrzeb", "Sprawdź zgłoszenia w sołectwach", "map", "Mikrowolontariat")
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
}

    companion object {
        fun getVillageCoordinates(village: String): Pair<Double, Double> {
            return when (village.trim().lowercase()) {
                "wieliszew" -> 52.4485 to 20.9785
                "łajski", "lajski" -> 52.4278 to 20.9632
                "skrzeszew" -> 52.4412 to 20.9389
                "michałów-reginów", "michalow-reginow" -> 52.4190 to 20.9950
                "komornica" -> 52.4705 to 20.9410
                "kałuszyn", "kaluszyn" -> 52.4632 to 20.9150
                "krubin" -> 52.4820 to 20.9150
                "janówek pierwszy", "janowek pierwszy" -> 52.4415 to 20.8750
                "poddębie", "poddebie" -> 52.4880 to 20.9850
                "sikory" -> 52.4750 to 20.9920
                "topolina" -> 52.4650 to 20.9990
                "olszewnica stara" -> 52.4420 to 20.9050
                "olszewnica nowa" -> 52.4330 to 20.9020
                else -> 52.4485 to 20.9785
            }
        }

        fun initialNotifications() = listOf(
            MunicipalNotification(
                id = "n_01",
                title = "Ostrzeżenie IMGW: Silny wiatr nad Zalewem Zegrzyńskim",
                description = "Zalecana ostrożność dla spacerowiczów w rejonie wałów w Wieliszewie i Komornicy.",
                timeAgo = "10 min temu",
                isAlert = true
            ),
            MunicipalNotification(
                id = "n_02",
                title = "Ruszyły zapisy na bezpłatną rehabilitację domową CUS",
                description = "Dla seniorów 70+ z orzeczoną barierą mobilności. Skontaktuj się z koordynatorem.",
                timeAgo = "Dziś, 09:30",
                isAlert = false
            )
        )

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
                title = "Smartfon bez tajemnic – Warsztaty Cyfrowe",
                category = "Edukacja Cyfrowa",
                dateText = "Środa, 12 Listopada, godz. 11:30",
                location = "Biblioteka Publiczna w Skrzeszewie",
                description = "Praktyczna nauka obsługi e-Karty Mieszkańca, wideorozmów z lekarzem, e-recepty i bezpieczeństwa w sieci.",
                spotsTotal = 15,
                spotsTaken = 8,
                isRegistered = false
            ),
            CusEvent(
                id = "ev_03",
                title = "Kawiarenka Międzypokoleniowa & Brydż",
                category = "Integracja Społeczna",
                dateText = "Piątki, godz. 16:00",
                location = "Gminne Centrum Kultury w Łajskach",
                description = "Spotkanie przy kawie i herbacie, wymiana doświadczeń międzypokoleniowych, gry planszowe i brydż.",
                spotsTotal = 30,
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
                approximateAddress = "Rejon ul. Kościelnej i Parku Świętojańskiego",
                description = "Pani Maria (82 l.) po zabiegu kolana prosi o pomoc w odebraniu leków z apteki i wniesieniu 2 zgrzewek wody do mieszkania.",
                pointsReward = 50,
                latitude = 52.4485,
                longitude = 20.9785
            ),
            NeedPin(
                id = "need_02",
                title = "Podwiezienie do przychodni w Legionowie",
                category = NeedCategory.TRANSPORT,
                urgency = Urgency.THIS_WEEK,
                village = "Skrzeszew",
                approximateAddress = "Rejon ul. Polnej i Partyzantów",
                description = "Pan Stanisław potrzebuje transportu na badanie USG we wtorek o 9:30 z powrotem do Skrzeszewa. Porusza się o lasce.",
                pointsReward = 80,
                latitude = 52.4412,
                longitude = 20.9389
            ),
            NeedPin(
                id = "need_03",
                title = "Drobna pomoc: konfiguracja nowego dekodera TV",
                category = NeedCategory.REPAIR,
                urgency = Urgency.FLEXIBLE,
                village = "Michałów-Reginów",
                approximateAddress = "Okolice stacji PKP Michałów-Reginów",
                description = "Podłączenie kabla HDMI i przeskanowanie nowych pasm telewizji naziemnej dla starszego małżeństwa.",
                pointsReward = 45,
                latitude = 52.4190,
                longitude = 20.9950
            ),
            NeedPin(
                id = "need_04",
                title = "Wyprowadzenie psa rasy Beagle po południu",
                category = NeedCategory.PET_CARE,
                urgency = Urgency.TODAY,
                village = "Łajski",
                approximateAddress = "Rejon ul. Sikorskiego",
                description = "Krótki 30-minutowy spacer z łagodnym psem. Właścicielka leży z grypą i potrzebuje wsparcia.",
                pointsReward = 35,
                latitude = 52.4278,
                longitude = 20.9632
            )
        )

        fun initialTimeBankEntries() = listOf(
            TimeBankEntry(
                id = "tb_01",
                title = "Oferuję: Naprawy hydrauliczne i drobna złota rączka",
                authorName = "Tadeusz N.",
                village = "Wieliszew",
                type = TimeBankType.OFFERING,
                hours = 1.0,
                category = "Złota rączka",
                description = "Wymiana uszczelek, naprawa cieknącej baterii, regulacja zawiasów w drzwiach. 1h za 1h w banku czasu.",
                rating = 5.0
            ),
            TimeBankEntry(
                id = "tb_02",
                title = "Oferuję: Cierpliwa nauka obsługi smartfona i komputera",
                authorName = "Magdalena K.",
                village = "Michałów-Reginów",
                type = TimeBankType.OFFERING,
                hours = 1.0,
                category = "Edukacja cyfrowa",
                description = "Nauka pisania SMS, wideorozmów WhatsApp z wnukami, bezpiecznego logowania do portalu pacjenta.",
                rating = 4.9
            ),
            TimeBankEntry(
                id = "tb_03",
                title = "Poszukuję: Pomoc w zgrabieniu liści w ogrodzie przed zimą",
                authorName = "Helena W.",
                village = "Skrzeszew",
                type = TimeBankType.REQUESTING,
                hours = 2.0,
                category = "Ogród",
                description = "Niewielki ogródek. Zapewniam grabie, worki, ciepłą herbatę z imbirem i domowe ciasto drożdżowe!",
                rating = 4.8
            )
        )

        fun initialBadges() = listOf(
            Badge(
                id = "bdg_01",
                title = "Serce Wieliszewa",
                description = "Przyznawana za realizację co najmniej 3 wolontariackich akcji pomocy seniorom w gminie.",
                iconEmoji = "❤️",
                isUnlocked = true,
                unlockedDate = "15.10.2026"
            ),
            Badge(
                id = "bdg_02",
                title = "Aktywny Sąsiad",
                description = "Przyznawana za pierwszą wymianę umiejętności w Lokalnym Banku Czasu.",
                iconEmoji = "🤝",
                isUnlocked = true,
                unlockedDate = "22.10.2026"
            ),
            Badge(
                id = "bdg_03",
                title = "Mistrz Ekologii",
                description = "Aktywny udział w Jadłodzielni Wieliszew i dzielenie się nadwyżkami żywności.",
                iconEmoji = "🌱",
                isUnlocked = true,
                unlockedDate = "28.10.2026"
            ),
            Badge(
                id = "bdg_04",
                title = "Promotor Zdrowia",
                description = "Udział w co najmniej 3 warsztatach prozdrowotnych i profilaktycznych CUS.",
                iconEmoji = "🏅",
                isUnlocked = false
            ),
            Badge(
                id = "bdg_05",
                title = "Lider Społeczności",
                description = "Zdobycie 600 punktów aktywności społecznej w Gminie Wieliszew.",
                iconEmoji = "⭐",
                isUnlocked = false
            )
        )

        fun initialRewards() = listOf(
            GamificationReward(
                id = "rew_01",
                title = "Karnet 1h: Kajak lub Rower Wodny nad Narwią",
                partnerName = "OSiR & Przystań Wieliszew",
                pointsCost = 250,
                discountPercent = 100,
                description = "Bezpłatne wypożyczenie kajaka 2-osobowego lub rowerka wodnego na 1 godzinę rekreacji wodnej.",
                category = "Sport i Rekreacja"
            ),
            GamificationReward(
                id = "rew_02",
                title = "Zniżka 30% na bilet na spektakl lub koncert",
                partnerName = "Gminne Centrum Kultury w Łajskach",
                pointsCost = 150,
                discountPercent = 30,
                description = "Zniżka na dowolne wydarzenie biletowane w repertuarze GCK Łajski w bieżącym kwartale.",
                category = "Kultura"
            ),
            GamificationReward(
                id = "rew_03",
                title = "Darmowa kawa sąsiedzka i domowe ciastko",
                partnerName = "Kawiarnia Przystań Wieliszew",
                pointsCost = 100,
                discountPercent = 100,
                description = "Kawa z ekspresu (lub herbata zimowa) oraz świeży wypiek dnia.",
                category = "Gastronomia"
            )
        )

        fun initialSpecialists() = listOf(
            TelehealthSpecialist(
                id = "sp_01",
                name = "mgr Anna Wiśniewska",
                type = SpecialistType.PSYCHOLOGIST,
                availableToday = true,
                nextFreeSlot = "Dzisiaj, godz. 13:30",
                bio = "Wsparcie psychologiczne dla seniorów i ich rodzin, praca ze stresem, żałobą, lękiem i poczuciem osamotnienia."
            ),
            TelehealthSpecialist(
                id = "sp_02",
                name = "mec. Piotr Dąbrowski",
                type = SpecialistType.LAWYER,
                availableToday = true,
                nextFreeSlot = "Dzisiaj, godz. 15:00",
                bio = "Nieodpłatna pomoc prawna: sprawy majątkowe, spadkowe, testamenty, alimentacyjne, dodatki mieszkaniowe i socjalne."
            ),
            TelehealthSpecialist(
                id = "sp_03",
                name = "mgr Elżbieta Zielińska",
                type = SpecialistType.SOCIAL_WORKER,
                availableToday = false,
                nextFreeSlot = "Jutro, godz. 09:00",
                bio = "Koordynator indywidualnych planów usług społecznych. Pomoc w uzyskaniu asystenta osobistego osoby z niepełnosprawnością (AOON) i opieki wytchnieniowej."
            )
        )

        fun initialSharedItems() = listOf(
            SharedItem(
                id = "sh_01",
                title = "Wózek inwalidzki lekki aluminiowy (składany)",
                category = ShareCategory.REHAB_EQUIPMENT,
                pickupLocation = "Magazyn CUS Wieliszew, ul. Modlińska 65",
                availableCount = 2,
                condition = "Atestowany, po dezynfekcji",
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
