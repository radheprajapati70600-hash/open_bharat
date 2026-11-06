package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class WalletRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("open_bharat_prefs", Context.MODE_PRIVATE)

    private val _points = MutableStateFlow(0)
    val points: StateFlow<Int> = _points.asStateFlow()

    private val _transactions = MutableStateFlow<List<WalletTransaction>>(emptyList())
    val transactions: StateFlow<List<WalletTransaction>> = _transactions.asStateFlow()

    private val _redemptions = MutableStateFlow<List<RedemptionRecord>>(emptyList())
    val redemptions: StateFlow<List<RedemptionRecord>> = _redemptions.asStateFlow()

    private val _isDailyCheckedIn = MutableStateFlow(false)
    val isDailyCheckedIn: StateFlow<Boolean> = _isDailyCheckedIn.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        val currentPts = prefs.getInt(KEY_POINTS, 20) // Give 20 welcome points for new user
        _points.value = currentPts

        // Check if checked in today
        val todayStr = getTodayDateString()
        val lastCheckin = prefs.getString(KEY_LAST_CHECKIN, "")
        _isDailyCheckedIn.value = (todayStr == lastCheckin)

        // Load transactions
        val txJson = prefs.getString(KEY_TRANSACTIONS, "[]") ?: "[]"
        _transactions.value = parseTransactions(txJson)

        // Load redemptions
        val redJson = prefs.getString(KEY_REDEMPTIONS, "[]") ?: "[]"
        _redemptions.value = parseRedemptions(redJson)
    }

    fun addPointsForSearch(): Int {
        val pointsToAdd = 10
        val newTotal = _points.value + pointsToAdd
        _points.value = newTotal
        prefs.edit().putInt(KEY_POINTS, newTotal).apply()

        addTransaction(
            title = "गूगल सर्च रिवॉर्ड (Search Bonus)",
            points = pointsToAdd,
            isEarn = true
        )
        return pointsToAdd
    }

    fun claimDailyBonus(): Boolean {
        val todayStr = getTodayDateString()
        val lastCheckin = prefs.getString(KEY_LAST_CHECKIN, "")
        if (todayStr == lastCheckin) {
            return false
        }

        val bonus = 25
        val newTotal = _points.value + bonus
        _points.value = newTotal
        _isDailyCheckedIn.value = true

        prefs.edit()
            .putInt(KEY_POINTS, newTotal)
            .putString(KEY_LAST_CHECKIN, todayStr)
            .apply()

        addTransaction(
            title = "दैनिक उपस्थिति बोनस (Daily Check-in)",
            points = bonus,
            isEarn = true
        )
        return true
    }

    fun addPointsForReadingShayari(): Int {
        val pointsToAdd = 5
        val newTotal = _points.value + pointsToAdd
        _points.value = newTotal
        prefs.edit().putInt(KEY_POINTS, newTotal).apply()

        addTransaction(
            title = "शायरी प्रेरणा रिवॉर्ड (Shayari Reading)",
            points = pointsToAdd,
            isEarn = true
        )
        return pointsToAdd
    }

    fun redeemReward(
        option: RedemptionOption,
        targetAccount: String,
        operator: String
    ): Boolean {
        if (_points.value < option.pointsRequired) {
            return false
        }

        val newTotal = _points.value - option.pointsRequired
        _points.value = newTotal
        prefs.edit().putInt(KEY_POINTS, newTotal).apply()

        // Add Transaction
        addTransaction(
            title = "${option.title} (${option.description})",
            points = -option.pointsRequired,
            isEarn = false
        )

        // Add Redemption Record
        val record = RedemptionRecord(
            id = "OB-" + UUID.randomUUID().toString().take(8).uppercase(),
            title = option.title,
            targetAccount = targetAccount,
            operatorOrProvider = operator,
            amountInr = option.amountInr,
            pointsCost = option.pointsRequired,
            timestamp = System.currentTimeMillis(),
            status = "सफल (Success)"
        )

        val updatedRedemptions = listOf(record) + _redemptions.value
        _redemptions.value = updatedRedemptions
        saveRedemptions(updatedRedemptions)

        return true
    }

    private fun addTransaction(title: String, points: Int, isEarn: Boolean) {
        val tx = WalletTransaction(
            id = UUID.randomUUID().toString(),
            title = title,
            pointsChange = points,
            timestamp = System.currentTimeMillis(),
            isEarn = isEarn
        )
        val updated = listOf(tx) + _transactions.value.take(49)
        _transactions.value = updated
        saveTransactions(updated)
    }

    private fun saveTransactions(list: List<WalletTransaction>) {
        val array = JSONArray()
        list.forEach { tx ->
            val obj = JSONObject().apply {
                put("id", tx.id)
                put("title", tx.title)
                put("pointsChange", tx.pointsChange)
                put("timestamp", tx.timestamp)
                put("isEarn", tx.isEarn)
            }
            array.put(obj)
        }
        prefs.edit().putString(KEY_TRANSACTIONS, array.toString()).apply()
    }

    private fun parseTransactions(json: String): List<WalletTransaction> {
        val list = mutableListOf<WalletTransaction>()
        try {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    WalletTransaction(
                        id = obj.optString("id", UUID.randomUUID().toString()),
                        title = obj.optString("title", ""),
                        pointsChange = obj.optInt("pointsChange", 0),
                        timestamp = obj.optLong("timestamp", System.currentTimeMillis()),
                        isEarn = obj.optBoolean("isEarn", true)
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    private fun saveRedemptions(list: List<RedemptionRecord>) {
        val array = JSONArray()
        list.forEach { r ->
            val obj = JSONObject().apply {
                put("id", r.id)
                put("title", r.title)
                put("targetAccount", r.targetAccount)
                put("operatorOrProvider", r.operatorOrProvider)
                put("amountInr", r.amountInr)
                put("pointsCost", r.pointsCost)
                put("timestamp", r.timestamp)
                put("status", r.status)
            }
            array.put(obj)
        }
        prefs.edit().putString(KEY_REDEMPTIONS, array.toString()).apply()
    }

    private fun parseRedemptions(json: String): List<RedemptionRecord> {
        val list = mutableListOf<RedemptionRecord>()
        try {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    RedemptionRecord(
                        id = obj.optString("id", ""),
                        title = obj.optString("title", ""),
                        targetAccount = obj.optString("targetAccount", ""),
                        operatorOrProvider = obj.optString("operatorOrProvider", ""),
                        amountInr = obj.optInt("amountInr", 0),
                        pointsCost = obj.optInt("pointsCost", 0),
                        timestamp = obj.optLong("timestamp", System.currentTimeMillis()),
                        status = obj.optString("status", "सफल (Success)")
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    private fun getTodayDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    companion object {
        private const val KEY_POINTS = "user_points"
        private const val KEY_LAST_CHECKIN = "last_checkin_date"
        private const val KEY_TRANSACTIONS = "wallet_transactions"
        private const val KEY_REDEMPTIONS = "redemptions_history"
    }
}
