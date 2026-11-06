package com.example.data

data class QuickApp(
    val id: String,
    val name: String,
    val url: String,
    val iconEmoji: String,
    val topBorderColorHex: Long = 0xFFFF9933,
    val isCustom: Boolean = false
)

data class Shayari(
    val id: Int,
    val title: String,
    val lines: List<String>,
    val author: String = "अज्ञात (Anonymous)",
    val category: String = "प्रेरणा (Inspiration)"
)

data class WalletTransaction(
    val id: String,
    val title: String,
    val pointsChange: Int,
    val timestamp: Long,
    val isEarn: Boolean
)

data class RedemptionOption(
    val id: String,
    val title: String,
    val description: String,
    val pointsRequired: Int,
    val amountInr: Int,
    val type: RedemptionType
)

enum class RedemptionType {
    MOBILE_RECHARGE,
    UPI_CASH,
    SHOPPING_VOUCHER
}

data class RedemptionRecord(
    val id: String,
    val title: String,
    val targetAccount: String,
    val operatorOrProvider: String,
    val amountInr: Int,
    val pointsCost: Int,
    val timestamp: Long,
    val status: String = "सफल (Success)"
)
