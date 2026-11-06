package com.example.data

object QuickAppRepository {
    val defaultApps: List<QuickApp> = listOf(
        QuickApp(
            id = "youtube",
            name = "यूट्यूब",
            url = "https://youtube.com",
            iconEmoji = "📺",
            topBorderColorHex = 0xFFFF0000
        ),
        QuickApp(
            id = "gmail",
            name = "जीमेल",
            url = "https://mail.google.com",
            iconEmoji = "✉️",
            topBorderColorHex = 0xFFEA4335
        ),
        QuickApp(
            id = "facebook",
            name = "फेसबुक",
            url = "https://facebook.com",
            iconEmoji = "👥",
            topBorderColorHex = 0xFF1877F2
        ),
        QuickApp(
            id = "wikipedia",
            name = "विकिपीडिया",
            url = "https://hi.wikipedia.org",
            iconEmoji = "📖",
            topBorderColorHex = 0xFF333333
        ),
        QuickApp(
            id = "news",
            name = "समाचार",
            url = "https://news.google.com",
            iconEmoji = "📰",
            topBorderColorHex = 0xFFFBBC05
        ),
        QuickApp(
            id = "irctc",
            name = "रेलवे IRCTC",
            url = "https://www.irctc.co.in",
            iconEmoji = "🚆",
            topBorderColorHex = 0xFF003399
        ),
        QuickApp(
            id = "cricbuzz",
            name = "क्रिकबज",
            url = "https://m.cricbuzz.com",
            iconEmoji = "🏏",
            topBorderColorHex = 0xFF138808
        ),
        QuickApp(
            id = "aajtak",
            name = "आज तक",
            url = "https://www.aajtak.in",
            iconEmoji = "📺",
            topBorderColorHex = 0xFFCC0000
        ),
        QuickApp(
            id = "sarkari",
            name = "सरकारी जॉब",
            url = "https://www.sarkariresult.com",
            iconEmoji = "📝",
            topBorderColorHex = 0xFF673AB7
        ),
        QuickApp(
            id = "flipkart",
            name = "फ्लिपकार्ट",
            url = "https://www.flipkart.com",
            iconEmoji = "🛍️",
            topBorderColorHex = 0xFF2874F0
        )
    )

    val rewardOptions: List<RedemptionOption> = listOf(
        RedemptionOption(
            id = "recharge_10",
            title = "मोबाइल रिचार्ज ₹10",
            description = "Jio / Airtel / Vi / BSNL टॉप-अप",
            pointsRequired = 100,
            amountInr = 10,
            type = RedemptionType.MOBILE_RECHARGE
        ),
        RedemptionOption(
            id = "recharge_20",
            title = "मोबाइल रिचार्ज ₹20",
            description = "टॉकटाइम या 1GB डेटा पैक",
            pointsRequired = 200,
            amountInr = 20,
            type = RedemptionType.MOBILE_RECHARGE
        ),
        RedemptionOption(
            id = "recharge_50",
            title = "मोबाइल रिचार्ज ₹50",
            description = "फुल टॉकटाइम या 3GB डेटा पैक",
            pointsRequired = 500,
            amountInr = 50,
            type = RedemptionType.MOBILE_RECHARGE
        ),
        RedemptionOption(
            id = "upi_25",
            title = "सीधा UPI कैश ₹25",
            description = "Google Pay / PhonePe / Paytm UPI",
            pointsRequired = 250,
            amountInr = 25,
            type = RedemptionType.UPI_CASH
        ),
        RedemptionOption(
            id = "upi_50",
            title = "सीधा UPI कैश ₹50",
            description = "तुरंत बैंक खाते में UPI ट्रांसफर",
            pointsRequired = 500,
            amountInr = 50,
            type = RedemptionType.UPI_CASH
        ),
        RedemptionOption(
            id = "amazon_100",
            title = "अमेज़न वाउचर ₹100",
            description = "Amazon Shopping Gift Card",
            pointsRequired = 900,
            amountInr = 100,
            type = RedemptionType.SHOPPING_VOUCHER
        )
    )
}
