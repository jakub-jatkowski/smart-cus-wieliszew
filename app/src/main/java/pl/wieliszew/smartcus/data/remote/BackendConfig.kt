package pl.wieliszew.smartcus.data.remote

object BackendConfig {
    // Default URL: 10.0.2.2 points to localhost of the host machine from Android Emulator
    // For physical phones connected to same Wi-Fi, change this to host machine IP (e.g. http://192.168.1.50:8000)
    var baseUrl: String = "http://10.0.2.2:8000"

    const val HEALTH_ENDPOINT = "/api/health"
    const val RESIDENT_ENDPOINT = "/api/resident"
    const val NOTIFICATIONS_ENDPOINT = "/api/notifications"
    const val NEED_PINS_ENDPOINT = "/api/need-pins"
    const val TIME_BANK_ENDPOINT = "/api/time-bank"
    const val EQUIPMENT_ENDPOINT = "/api/equipment"
    const val JADLODZIELNIA_ENDPOINT = "/api/jadlodzielnia"
    const val TELEHEALTH_ENDPOINT = "/api/telehealth"
    const val VOUCHERS_ENDPOINT = "/api/vouchers"
    const val GAMIFICATION_CLAIM_ENDPOINT = "/api/gamification/claim-voucher"
    const val CHAT_ENDPOINT = "/api/assistant/chat"
}

