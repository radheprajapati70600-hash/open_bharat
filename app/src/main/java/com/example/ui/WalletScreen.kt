package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.QuickAppRepository
import com.example.data.RedemptionOption
import com.example.data.RedemptionType
import com.example.data.WalletRepository
import com.example.ui.theme.ChakraBlue
import com.example.ui.theme.IndiaGreen
import com.example.ui.theme.SaffronDark
import com.example.ui.theme.SaffronPrimary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletBottomSheet(
    walletRepository: WalletRepository,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val points by walletRepository.points.collectAsState()
    val isDailyCheckedIn by walletRepository.isDailyCheckedIn.collectAsState()
    val transactions by walletRepository.transactions.collectAsState()
    val redemptions by walletRepository.redemptions.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedOptionForRedeem by remember { mutableStateOf<RedemptionOption?>(null) }
    var showSuccessDialog by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "👛",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "ओपन भारत रिवॉर्ड वॉलेट",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("wallet_close_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "बंद करें"
                    )
                }
            }

            // Points Banner Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFFFF9933),
                                Color(0xFFFFB366),
                                Color(0xFF138808)
                            )
                        )
                    )
                    .padding(2.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Column {
                            Text(
                                text = "कुल उपलब्ध पॉइंट्स (Total Points)",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "$points",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = SaffronDark
                                )
                                Text(
                                    text = " Pts",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SaffronDark,
                                    modifier = Modifier.padding(bottom = 4.dp, start = 2.dp)
                                )
                            }
                            Text(
                                text = "लगभग मूल्य: ₹${points / 10}.00",
                                style = MaterialTheme.typography.labelSmall,
                                color = IndiaGreen,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        // Daily Check-in Button
                        Button(
                            onClick = {
                                walletRepository.claimDailyBonus()
                            },
                            enabled = !isDailyCheckedIn,
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = IndiaGreen,
                                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                            modifier = Modifier.testTag("daily_checkin_button")
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = if (isDailyCheckedIn) "आज प्राप्त ✅" else "दैनिक बोनस 🎁",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                                if (!isDailyCheckedIn) {
                                    Text(
                                        text = "+25 Pts",
                                        fontSize = 11.sp,
                                        color = Color.White.copy(alpha = 0.9f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Tabs
            PrimaryTabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp)
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("रिडीम (Redeem)", fontWeight = FontWeight.SemiBold) },
                    icon = { Icon(Icons.Default.CardGiftcard, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("कमाएं (Earn)", fontWeight = FontWeight.SemiBold) },
                    icon = { Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("इतिहास (History)", fontWeight = FontWeight.SemiBold) },
                    icon = { Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tab Content
            when (selectedTab) {
                0 -> RedeemTab(
                    points = points,
                    options = QuickAppRepository.rewardOptions,
                    onSelectOption = { selectedOptionForRedeem = it }
                )
                1 -> HowToEarnTab()
                2 -> HistoryTab(
                    transactions = transactions,
                    redemptions = redemptions
                )
            }
        }
    }

    // Modal Sheet for Entering Mobile / UPI Details
    selectedOptionForRedeem?.let { option ->
        RedemptionDialog(
            option = option,
            userPoints = points,
            onDismiss = { selectedOptionForRedeem = null },
            onConfirmRedeem = { target, operator ->
                val success = walletRepository.redeemReward(option, target, operator)
                selectedOptionForRedeem = null
                if (success) {
                    showSuccessDialog = "बधाई हो! ${option.title} का अनुरोध सफलतापूर्वक दर्ज कर लिया गया है।\nविवरण: $target ($operator)"
                }
            }
        )
    }

    // Success Confirmation Dialog
    showSuccessDialog?.let { message ->
        AlertDialog(
            onDismissRequest = { showSuccessDialog = null },
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = IndiaGreen,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    text = "रिचार्ज सफल! 🎉",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = { showSuccessDialog = null },
                    colors = ButtonDefaults.buttonColors(containerColor = IndiaGreen),
                    modifier = Modifier.testTag("success_dialog_confirm")
                ) {
                    Text("ठीक है (Done)")
                }
            }
        )
    }
}

@Composable
private fun RedeemTab(
    points: Int,
    options: List<RedemptionOption>,
    onSelectOption: (RedemptionOption) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.height(380.dp)
    ) {
        items(options, key = { it.id }) { option ->
            val hasEnoughPoints = points >= option.pointsRequired
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                border = if (hasEnoughPoints) borderStroke(1.dp, SaffronPrimary.copy(alpha = 0.5f)) else null,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectOption(option) }
                    .testTag("redeem_option_${option.id}")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(14.dp)
                ) {
                    // Icon
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(
                                when (option.type) {
                                    RedemptionType.MOBILE_RECHARGE -> Color(0xFFFFE8D6)
                                    RedemptionType.UPI_CASH -> Color(0xFFD9F2D8)
                                    RedemptionType.SHOPPING_VOUCHER -> Color(0xFFE3F2FD)
                                }
                            )
                    ) {
                        Icon(
                            imageVector = when (option.type) {
                                RedemptionType.MOBILE_RECHARGE -> Icons.Default.PhoneAndroid
                                RedemptionType.UPI_CASH -> Icons.Default.CurrencyRupee
                                RedemptionType.SHOPPING_VOUCHER -> Icons.Default.CardGiftcard
                            },
                            contentDescription = null,
                            tint = when (option.type) {
                                RedemptionType.MOBILE_RECHARGE -> SaffronDark
                                RedemptionType.UPI_CASH -> IndiaGreen
                                RedemptionType.SHOPPING_VOUCHER -> ChakraBlue
                            },
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = option.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = option.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (hasEnoughPoints) SaffronPrimary else MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = "${option.pointsRequired} Pts",
                                color = if (hasEnoughPoints) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HowToEarnTab() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .height(380.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        EarnMethodCard(
            emoji = "🔍",
            title = "गूगल सर्च करें (Search Web)",
            desc = "हर एक सर्च पर तुरंत 10 पॉइंट्स पाएं। आप जितना सर्च करेंगे, उतने ज्यादा पॉइंट्स जुड़ेंगे!",
            pointsBadge = "+10 Pts प्रति सर्च"
        )
        EarnMethodCard(
            emoji = "📅",
            title = "दैनिक उपस्थिति (Daily Check-in)",
            desc = "रोज़ाना ओपन भारत ऐप खोलें और उपस्थिति दर्ज कर 25 पॉइंट्स प्राप्त करें।",
            pointsBadge = "+25 Pts रोज़ाना"
        )
        EarnMethodCard(
            emoji = "✨",
            title = "प्रेरणादायक विचार पढ़ें (Read Shayari)",
            desc = "आज की प्रेरणादायक शायरी पढ़कर ज्ञान और आत्मविश्वास के साथ पॉइंट्स अर्जित करें।",
            pointsBadge = "+5 Pts"
        )
        EarnMethodCard(
            emoji = "📱",
            title = "मुफ्त मोबाइल रिचार्ज पाएं",
            desc = "100 पॉइंट्स होते ही आप Jio, Airtel, Vi या BSNL का रिचार्ज प्राप्त कर सकते हैं।",
            pointsBadge = "100% सुरक्षित"
        )
    }
}

@Composable
private fun EarnMethodCard(
    emoji: String,
    title: String,
    desc: String,
    pointsBadge: String
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = emoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = IndiaGreen.copy(alpha = 0.15f)
            ) {
                Text(
                    text = pointsBadge,
                    color = IndiaGreen,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun HistoryTab(
    transactions: List<com.example.data.WalletTransaction>,
    redemptions: List<com.example.data.RedemptionRecord>
) {
    val sdf = remember { SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()) }

    if (transactions.isEmpty() && redemptions.isEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
        ) {
            Text(
                text = "अभी कोई लेन-देन नहीं है।\nसर्च करके पॉइंट्स कमाना शुरू करें!",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.height(380.dp)
        ) {
            // First show recent redemptions
            if (redemptions.isNotEmpty()) {
                item {
                    Text(
                        text = "रिचार्ज व रिडीम अनुरोध (Redemptions)",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = SaffronDark,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                items(redemptions, key = { it.id }) { red ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(text = "🎁", fontSize = 22.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = red.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${red.targetAccount} • ${red.operatorOrProvider}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = sdf.format(Date(red.timestamp)),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "-${red.pointsCost} Pts",
                                    color = Color(0xFFD32F2F),
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = IndiaGreen.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = red.status,
                                        color = IndiaGreen,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Then point transactions
            if (transactions.isNotEmpty()) {
                item {
                    Text(
                        text = "पॉइंट्स इतिहास (Point Earnings)",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = IndiaGreen,
                        modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
                    )
                }
                items(transactions, key = { it.id }) { tx ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(
                                    if (tx.isEarn) IndiaGreen.copy(alpha = 0.15f)
                                    else Color(0xFFFFEBEE)
                                )
                        ) {
                            Text(
                                text = if (tx.isEarn) "🪙" else "🔻",
                                fontSize = 16.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = tx.title,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = sdf.format(Date(tx.timestamp)),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = if (tx.isEarn) "+${tx.pointsChange} Pts" else "${tx.pointsChange} Pts",
                            color = if (tx.isEarn) IndiaGreen else Color(0xFFD32F2F),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RedemptionDialog(
    option: RedemptionOption,
    userPoints: Int,
    onDismiss: () -> Unit,
    onConfirmRedeem: (target: String, operator: String) -> Unit
) {
    var targetInput by remember { mutableStateOf("") }
    var selectedOperator by remember { mutableStateOf("Jio") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val operators = listOf("Jio", "Airtel", "Vi", "BSNL")
    val hasEnough = userPoints >= option.pointsRequired

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = option.title,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "आवश्यक पॉइंट्स: ${option.pointsRequired} Pts (आपके पास: $userPoints Pts)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (hasEnough) IndiaGreen else Color(0xFFD32F2F),
                    fontWeight = FontWeight.SemiBold
                )

                if (option.type == RedemptionType.MOBILE_RECHARGE) {
                    Text(
                        text = "मोबाइल ऑपरेटर चुनें:",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        operators.forEach { op ->
                            FilterChip(
                                selected = selectedOperator == op,
                                onClick = { selectedOperator = op },
                                label = { Text(op, fontSize = 12.sp) }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = targetInput,
                        onValueChange = {
                            if (it.length <= 10 && it.all { ch -> ch.isDigit() }) {
                                targetInput = it
                            }
                        },
                        label = { Text("10 अंकों का मोबाइल नंबर") },
                        placeholder = { Text("उदा. 9876543210") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        isError = errorMessage != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("redeem_mobile_input")
                    )
                } else if (option.type == RedemptionType.UPI_CASH) {
                    OutlinedTextField(
                        value = targetInput,
                        onValueChange = { targetInput = it },
                        label = { Text("UPI ID दर्ज करें") },
                        placeholder = { Text("उदा. yourname@okhdfcbank") },
                        singleLine = true,
                        isError = errorMessage != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("redeem_upi_input")
                    )
                } else {
                    OutlinedTextField(
                        value = targetInput,
                        onValueChange = { targetInput = it },
                        label = { Text("ईमेल या मोबाइल नंबर") },
                        placeholder = { Text("वाउचर कोड भेजने हेतु") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                errorMessage?.let {
                    Text(
                        text = it,
                        color = Color(0xFFD32F2F),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (!hasEnough) {
                        errorMessage = "पर्याप्त पॉइंट्स नहीं हैं! और पॉइंट्स कमाएं।"
                        return@Button
                    }
                    if (option.type == RedemptionType.MOBILE_RECHARGE && targetInput.length != 10) {
                        errorMessage = "कृपया सही 10 अंकों का मोबाइल नंबर दर्ज करें।"
                        return@Button
                    }
                    if (option.type == RedemptionType.UPI_CASH && !targetInput.contains("@")) {
                        errorMessage = "कृपया सही UPI ID दर्ज करें।"
                        return@Button
                    }
                    if (targetInput.isBlank()) {
                        errorMessage = "कृपया जानकारी भरें।"
                        return@Button
                    }
                    onConfirmRedeem(targetInput, selectedOperator)
                },
                colors = ButtonDefaults.buttonColors(containerColor = SaffronDark),
                modifier = Modifier.testTag("redeem_submit_button")
            ) {
                Text("रिडीम करें (Confirm)")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("रद्द करें")
            }
        }
    )
}

private fun borderStroke(width: androidx.compose.ui.unit.Dp, color: Color) =
    androidx.compose.foundation.BorderStroke(width, color)
