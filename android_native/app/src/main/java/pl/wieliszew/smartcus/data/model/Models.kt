package pl.wieliszew.smartcus.data.model

data class UserProfile(
    val id: String = "usr_001",
    val fullName: String = "Janina Kowalska",
    val village: String = "Wieliszew",
    val isSenior: Boolean = true,
    val isVolunteer: Boolean = true,
    val timeBankBalanceHours: Double = 5.5,
    val gamificationPoints: Int = 420,
    val levelTitle: String = "Zasłużony Sąsiad Wieliszewa"
)

data class ResidentCard(
    val cardNumber: String = "WKM-2026-88492",
    val holderName: String = "Janina Kowalska",
    val isActive: Boolean = true,
    val validUntil: String = "31.12.2028",
    val dynamicToken: String = "WKM-SEC-8921-X9",
    val isNfcReady: Boolean = true,
    val benefits: List<String> = listOf(
        "Zniżka 50% na basen i hale sportową w Wieliszewie",
        "Bezpłatne wejścia na warsztaty CUS Wieliszew",
        "Zniżka 20% na wypożyczalnie kajaków i rowerów wodnych",
        "Bezpłatna komunikacja lokalna L-komunikacja"
    )
)

data class CusEvent(
    val id: String,
    val title: String,
    val organizer: String = "Centrum Usług Społecznych Wieliszew",
    val category: String,
    val dateText: String,
    val location: String,
    val description: String,
    val spotsTotal: Int,
    val spotsTaken: Int,
    val isRegistered: Boolean = false,
    val seniorFriendly: Boolean = true
)

enum class NeedCategory(val label: String, val icon: String) {
    SHOPPING("Zakupy i apteka", "🛒"),
    TRANSPORT("Transport i dojazd", "🚗"),
    REPAIR("Drobna naprawa", "🔧"),
    COMPANIONSHIP("Rozmowa i spacer", "☕"),
    PET_CARE("Opieka nad psem/kotem", "🐕"),
    GARDEN("Ogród i odśnieżanie", "🌱")
}

enum class Urgency(val label: String) {
    TODAY("Pilne (dzisiaj)"),
    THIS_WEEK("W tym tygodniu"),
    FLEXIBLE("Elastyczny termin")
}

enum class NeedStatus {
    OPEN,
    IN_PROGRESS,
    COMPLETED
}

data class NeedPin(
    val id: String,
    val title: String,
    val category: NeedCategory,
    val urgency: Urgency,
    val village: String,
    val approximateAddress: String, // RODO safe approximate zone
    val description: String,
    val status: NeedStatus = NeedStatus.OPEN,
    val timeMinutesEstimate: Int = 45,
    val pointsReward: Int = 50,
    val acceptedByVolunteer: Boolean = false
)

enum class TimeBankType {
    OFFERING,
    REQUESTING
}

data class TimeBankEntry(
    val id: String,
    val title: String,
    val authorName: String,
    val village: String,
    val type: TimeBankType,
    val hours: Double,
    val category: String,
    val description: String,
    val rating: Double = 4.9
)

data class Badge(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val isUnlocked: Boolean,
    val unlockedDate: String? = null
)

data class GamificationReward(
    val id: String,
    val title: String,
    val partnerName: String,
    val pointsCost: Int,
    val discountPercent: Int,
    val description: String,
    val category: String
)

enum class SpecialistType(val label: String) {
    PSYCHOLOGIST("Psycholog CUS"),
    LAWYER("Radca Prawny / Pomoc Prawna"),
    SOCIAL_WORKER("Koordynator Usług Społecznych"),
    SENIOR_ADVISOR("Doradca ds. Seniorów i Wsparcia")
}

data class TelehealthSpecialist(
    val id: String,
    val name: String,
    val type: SpecialistType,
    val availableToday: Boolean,
    val nextFreeSlot: String,
    val bio: String
)

enum class ShareCategory(val label: String) {
    REHAB_EQUIPMENT("Sprzęt Rehabilitacyjny"),
    FOOD_SHARING("Jadłodzielnia Wieliszew"),
    CLOTHING_GOODS("Ubrania i Artykuły"),
    TOOLS_EQUIPMENT("Sprzęt domowy i ogrodowy")
}

data class SharedItem(
    val id: String,
    val title: String,
    val category: ShareCategory,
    val pickupLocation: String,
    val availableCount: Int,
    val isFree: Boolean = true,
    val condition: String,
    val description: String,
    val isReserved: Boolean = false
)

enum class MessageSender {
    USER,
    ASSISTANT
}

data class Recommendation(
    val id: String,
    val title: String,
    val subtitle: String,
    val targetModule: String,
    val reasonBadge: String
)

data class AiMessage(
    val id: String,
    val sender: MessageSender,
    val text: String,
    val timestamp: String,
    val recommendations: List<Recommendation> = emptyList()
)

