package pl.wieliszew.smartcus.data.remote

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

class CusApiService {

    private val gson = Gson()
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    private val client = OkHttpClient.Builder()
        .connectTimeout(3, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .build()

    suspend fun checkHealth(): Boolean = withContext(Dispatchers.IO) {
        try {
            val url = "${BackendConfig.baseUrl}${BackendConfig.HEALTH_ENDPOINT}"
            val request = Request.Builder().url(url).get().build()
            client.newCall(request).execute().use { response ->
                response.isSuccessful
            }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getResident(): Map<String, Any>? = withContext(Dispatchers.IO) {
        try {
            val url = "${BackendConfig.baseUrl}${BackendConfig.RESIDENT_ENDPOINT}"
            val request = Request.Builder().url(url).get().build()
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val body = response.body?.string() ?: return@use null
                    val type = object : TypeToken<Map<String, Any>>() {}.type
                    gson.fromJson<Map<String, Any>>(body, type)
                } else null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getNeedPins(): List<Map<String, Any>>? = withContext(Dispatchers.IO) {
        try {
            val url = "${BackendConfig.baseUrl}${BackendConfig.NEED_PINS_ENDPOINT}"
            val request = Request.Builder().url(url).get().build()
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val body = response.body?.string() ?: return@use null
                    val type = object : TypeToken<List<Map<String, Any>>>() {}.type
                    gson.fromJson<List<Map<String, Any>>>(body, type)
                } else null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun createNeedPin(title: String, description: String, village: String, category: String, urgency: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val url = "${BackendConfig.baseUrl}${BackendConfig.NEED_PINS_ENDPOINT}"
            val payload = mapOf(
                "title" to title,
                "description" to description,
                "village" to village,
                "category" to category,
                "urgency" to urgency,
                "points" to 50
            )
            val body = gson.toJson(payload).toRequestBody(jsonMediaType)
            val request = Request.Builder().url(url).post(body).build()
            client.newCall(request).execute().use { it.isSuccessful }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun acceptNeedPin(pinId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val url = "${BackendConfig.baseUrl}${BackendConfig.NEED_PINS_ENDPOINT}/$pinId/accept"
            val body = "".toRequestBody(jsonMediaType)
            val request = Request.Builder().url(url).post(body).build()
            client.newCall(request).execute().use { it.isSuccessful }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun completeNeedPin(pinId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val url = "${BackendConfig.baseUrl}${BackendConfig.NEED_PINS_ENDPOINT}/$pinId/complete"
            val body = "".toRequestBody(jsonMediaType)
            val request = Request.Builder().url(url).post(body).build()
            client.newCall(request).execute().use { it.isSuccessful }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getTimeBankEntries(): List<Map<String, Any>>? = withContext(Dispatchers.IO) {
        try {
            val url = "${BackendConfig.baseUrl}${BackendConfig.TIME_BANK_ENDPOINT}"
            val request = Request.Builder().url(url).get().build()
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val body = response.body?.string() ?: return@use null
                    val type = object : TypeToken<List<Map<String, Any>>>() {}.type
                    gson.fromJson<List<Map<String, Any>>>(body, type)
                } else null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun createTimeBankEntry(
        userName: String,
        village: String,
        type: String,
        category: String,
        title: String,
        description: String,
        hours: Double
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val url = "${BackendConfig.baseUrl}${BackendConfig.TIME_BANK_ENDPOINT}"
            val payload = mapOf(
                "user_name" to userName,
                "village" to village,
                "type" to type,
                "category" to category,
                "title" to title,
                "description" to description,
                "hours" to hours
            )
            val body = gson.toJson(payload).toRequestBody(jsonMediaType)
            val request = Request.Builder().url(url).post(body).build()
            client.newCall(request).execute().use { it.isSuccessful }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun exchangeTimeBank(entryId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val url = "${BackendConfig.baseUrl}${BackendConfig.TIME_BANK_ENDPOINT}/$entryId/exchange"
            val body = "".toRequestBody(jsonMediaType)
            val request = Request.Builder().url(url).post(body).build()
            client.newCall(request).execute().use { it.isSuccessful }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getEquipment(): List<Map<String, Any>>? = withContext(Dispatchers.IO) {
        try {
            val url = "${BackendConfig.baseUrl}${BackendConfig.EQUIPMENT_ENDPOINT}"
            val request = Request.Builder().url(url).get().build()
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val body = response.body?.string() ?: return@use null
                    val type = object : TypeToken<List<Map<String, Any>>>() {}.type
                    gson.fromJson<List<Map<String, Any>>>(body, type)
                } else null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun reserveEquipment(equipmentId: String): String? = withContext(Dispatchers.IO) {
        try {
            val url = "${BackendConfig.baseUrl}${BackendConfig.EQUIPMENT_ENDPOINT}/$equipmentId/reserve"
            val body = "".toRequestBody(jsonMediaType)
            val request = Request.Builder().url(url).post(body).build()
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val resBody = response.body?.string() ?: return@use null
                    val type = object : TypeToken<Map<String, Any>>() {}.type
                    val map = gson.fromJson<Map<String, Any>>(resBody, type)
                    map["pickup_code"] as? String
                } else null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getFoodSharing(): List<Map<String, Any>>? = withContext(Dispatchers.IO) {
        try {
            val url = "${BackendConfig.baseUrl}${BackendConfig.JADLODZIELNIA_ENDPOINT}"
            val request = Request.Builder().url(url).get().build()
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val body = response.body?.string() ?: return@use null
                    val type = object : TypeToken<List<Map<String, Any>>>() {}.type
                    gson.fromJson<List<Map<String, Any>>>(body, type)
                } else null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun donateFood(itemName: String, category: String, quantity: String, expiryDate: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val url = "${BackendConfig.baseUrl}${BackendConfig.JADLODZIELNIA_ENDPOINT}/donate"
            val payload = mapOf(
                "item_name" to itemName,
                "category" to category,
                "quantity" to quantity,
                "expiry_date" to expiryDate
            )
            val body = gson.toJson(payload).toRequestBody(jsonMediaType)
            val request = Request.Builder().url(url).post(body).build()
            client.newCall(request).execute().use { it.isSuccessful }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getNotifications(): List<Map<String, Any>>? = withContext(Dispatchers.IO) {
        try {
            val url = "${BackendConfig.baseUrl}${BackendConfig.NOTIFICATIONS_ENDPOINT}"
            val request = Request.Builder().url(url).get().build()
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val body = response.body?.string() ?: return@use null
                    val type = object : TypeToken<List<Map<String, Any>>>() {}.type
                    gson.fromJson<List<Map<String, Any>>>(body, type)
                } else null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getVouchers(): List<Map<String, Any>>? = withContext(Dispatchers.IO) {
        try {
            val url = "${BackendConfig.baseUrl}${BackendConfig.VOUCHERS_ENDPOINT}"
            val request = Request.Builder().url(url).get().build()
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val body = response.body?.string() ?: return@use null
                    val type = object : TypeToken<List<Map<String, Any>>>() {}.type
                    gson.fromJson<List<Map<String, Any>>>(body, type)
                } else null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun askAssistant(message: String): String? = withContext(Dispatchers.IO) {
        try {
            val url = "${BackendConfig.baseUrl}${BackendConfig.CHAT_ENDPOINT}"
            val payload = mapOf("message" to message)
            val body = gson.toJson(payload).toRequestBody(jsonMediaType)
            val request = Request.Builder().url(url).post(body).build()
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val resBody = response.body?.string() ?: return@use null
                    val type = object : TypeToken<Map<String, Any>>() {}.type
                    val map = gson.fromJson<Map<String, Any>>(resBody, type)
                    map["reply"] as? String
                } else null
            }
        } catch (e: Exception) {
            null
        }
    }
}

