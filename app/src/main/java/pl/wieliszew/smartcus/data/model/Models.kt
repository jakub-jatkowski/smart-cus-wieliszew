package pl.wieliszew.smartcus.data.model

data class UserProfile(
    val id: String = "usr_001",
    val fullName: String = "Janina Kowalska",
    val village: String = "Wieliszew",
    val iceContact: String = "Córka Anna: +48 601 234 567",
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
        "Zniżka 50% na basen i halę sportową w Wieliszewie",
        "Bezpłatne wejścia na warsztaty CUS Wieliszew",
        "Zniżka 20% na wypożyczalnie kajaków i rowerów wodnych",
        "Bezpłatna komunikacja lokalna L-komunikacja"
    )
)

data class Voucher(
    val id: String,
    val title: String,
    val partnerName: String,
    val code: String,
    val validUntil: String = "31.12.2026",
    val isUsed: Boolean = false
)

data class MunicipalNotification(
    val id: String,
    val title: String,
    val description: String,
    val timeAgo: String,
    val isAlert: Boolean = false,
    val isRead: Boolean = false
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
    SHOPPING("Zakupy / Apteka", "🛒"),
    TRANSPORT("Transport", "🚗"),
    REPAIR("Naprawa", "🔧"),
    COMPANIONSHIP("Spacer / Rozmowa", "☕"),
    PET_CARE("Opieka nad pupilem", "🐕"),
    GARDEN("Ogród / Prace", "🌱")
}

enum class Urgency(val label: String) {
    TODAY("Pilne (dziś)"),
    THIS_WEEK("Ten tydzień"),
    FLEXIBLE("Elastyczny")
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
    val acceptedByVolunteer: Boolean = false,
    val latitude: Double = 52.4485,
    val longitude: Double = 20.9785
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
    PSYCHOLOGIST("Psycholog"),
    LAWYER("Radca Prawny"),
    SOCIAL_WORKER("Koordynator CUS"),
    SENIOR_ADVISOR("Doradca Seniora")
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
    REHAB_EQUIPMENT("Sprzęt Medyczny"),
    FOOD_SHARING("Jadłodzielnia"),
    CLOTHING_GOODS("Ubrania i Rzeczy"),
    TOOLS_EQUIPMENT("Narzędzia i Dom")
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
    val isReserved: Boolean = false,
    val reservationCode: String? = null
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
