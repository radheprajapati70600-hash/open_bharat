package com.example.data

object ShayariRepository {
    val shayaris: List<Shayari> = listOf(
        Shayari(
            id = 1,
            title = "✨ आज की प्रेरणादायक शायरी ✨",
            lines = listOf(
                "लहरों से डरकर नौका पार नहीं होती,",
                "कोशिश करने वालों की कभी हार नहीं होती।"
            ),
            author = "सोहन लाल द्विवेदी / हरिवंश राय बच्चन",
            category = "साहस और संघर्ष"
        ),
        Shayari(
            id = 2,
            title = "🌟 उम्मीद की किरण 🌟",
            lines = listOf(
                "हो गई है पीर पर्वत-सी पिघलनी चाहिए,",
                "इस हिमालय से कोई गंगा निकलनी चाहिए।"
            ),
            author = "दुष्यंत कुमार",
            category = "बदलाव"
        ),
        Shayari(
            id = 3,
            title = "🔥 कर्म ही विजय है 🔥",
            lines = listOf(
                "मंजिलें उन्हीं को मिलती हैं, जिनके सपनों में जान होती है,",
                "पंखों से कुछ नहीं होता, हौसलों से उड़ान होती है!"
            ),
            author = "अज्ञात",
            category = "हौसला"
        ),
        Shayari(
            id = 4,
            title = "🌅 नया सवेरा 🌅",
            lines = listOf(
                "रात जितनी गहरी होती है,",
                "सवेरा उतना ही करीब होता है।"
            ),
            author = "सुविचार",
            category = "आशा"
        ),
        Shayari(
            id = 5,
            title = "🎯 लक्ष्य प्राप्ति 🎯",
            lines = listOf(
                "उठो, जागो और तब तक मत रुको,",
                "जब तक लक्ष्य की प्राप्ति न हो जाए।"
            ),
            author = "स्वामी विवेकानंद",
            category = "प्रेरणा"
        ),
        Shayari(
            id = 6,
            title = "🌱 मेहनत का फल 🌱",
            lines = listOf(
                "सफलता एक दिन में नहीं मिलती,",
                "मगर ठान लो तो एक दिन जरूर मिलती है।"
            ),
            author = "प्रेरक विचार",
            category = "लगन"
        ),
        Shayari(
            id = 7,
            title = "💎 आत्मबल 💎",
            lines = listOf(
                "तू खुद की खोज में निकल, तू किसलिए हताश है,",
                "तू चल तेरे वजूद की समय को भी तलाश है।"
            ),
            author = "तनु वेड्स मनु / गीत",
            category = "आत्मविश्वास"
        )
    )

    fun getRandomShayari(excludeId: Int = -1): Shayari {
        val candidates = if (shayaris.size > 1) shayaris.filter { it.id != excludeId } else shayaris
        return candidates.random()
    }
}
