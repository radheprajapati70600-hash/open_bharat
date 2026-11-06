package com.example.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.QuickApp
import com.example.data.QuickAppRepository
import com.example.data.Shayari
import com.example.data.ShayariRepository
import com.example.data.WalletRepository
import com.example.ui.theme.IndiaGreen
import com.example.ui.theme.SaffronDark
import com.example.ui.theme.SaffronPrimary
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    walletRepository: WalletRepository,
    onNavigateToUrl: (String) -> Unit,
    onOpenWallet: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }

    val userPoints by walletRepository.points.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var currentShayariIndex by remember { mutableIntStateOf(0) }
    var showMoreApps by remember { mutableStateOf(false) }

    val currentShayari = ShayariRepository.shayaris[currentShayariIndex % ShayariRepository.shayaris.size]

    val triggerSearch: () -> Unit = {
        val trimmed = searchQuery.trim()
        if (trimmed.isNotBlank()) {
            keyboardController?.hide()
            // Award points for search
            val earned = walletRepository.addPointsForSearch()
            scope.launch {
                snackbarHostState.showSnackbar("🎉 +$earned पॉइंट्स मिले! कुल: ${userPoints + earned} Pts")
            }
            // If query is an URL, load it directly, else perform Google Search
            val targetUrl = when {
                trimmed.startsWith("http://") || trimmed.startsWith("https://") -> trimmed
                trimmed.contains(".") && !trimmed.contains(" ") -> "https://$trimmed"
                else -> "https://www.google.com/search?q=" + Uri.encode(trimmed)
            }
            onNavigateToUrl(targetUrl)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // 1. Tricolor Header Bar
            item {
                HeaderBar(
                    points = userPoints,
                    onWalletClick = onOpenWallet
                )
            }

            // Center Column layout for comfortable tablet and phone reading
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .widthIn(max = 600.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 2. Open Bharat Banner Art
                    Spacer(modifier = Modifier.height(14.dp))
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                    ) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_bharat_banner),
                                contentDescription = "ओपन भारत बैनर",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp)
                            )
                            // Subtle gradient overlay with welcome motto
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                Color.Transparent,
                                                Color.Black.copy(alpha = 0.65f)
                                            )
                                        )
                                    )
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                contentAlignment = Alignment.BottomStart
                            ) {
                                Column {
                                    Text(
                                        text = "भारत का अपना ब्राउज़र (Open Bharat)",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = "सर्च करें, ज्ञान पाएं और मोबाइल रिचार्ज जीतें!",
                                        color = Color.White.copy(alpha = 0.9f),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }

                    // 3. Search Bar Container matching original web app styling
                    Spacer(modifier = Modifier.height(20.dp))
                    SearchContainer(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        onSearch = triggerSearch
                    )

                    // 4. Quick Apps Grid (5 essential apps from original HTML)
                    Spacer(modifier = Modifier.height(22.dp))
                    Text(
                        text = "🚀 जरूरी ऐप्स (Essential Apps)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp, vertical = 4.dp)
                    )

                    AppsGrid(
                        apps = if (showMoreApps) QuickAppRepository.defaultApps else QuickAppRepository.defaultApps.take(5),
                        onAppClick = { app ->
                            walletRepository.addPointsForSearch()
                            scope.launch {
                                snackbarHostState.showSnackbar("🎉 +10 पॉइंट्स मिले! ${app.name} लोड हो रहा है…")
                            }
                            onNavigateToUrl(app.url)
                        }
                    )

                    // Toggle for More Apps
                    TextButton(
                        onClick = { showMoreApps = !showMoreApps },
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = if (showMoreApps) "कम दिखाएं" else "और भारतीय सेवाएं देखें ▾",
                            color = SaffronDark,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }

                    // 5. Daily Inspiring Shayari Card (✨ आज की प्रेरणादायक शायरी ✨)
                    Spacer(modifier = Modifier.height(16.dp))
                    ShayariCard(
                        shayari = currentShayari,
                        onNext = {
                            currentShayariIndex++
                            val earned = walletRepository.addPointsForReadingShayari()
                            scope.launch {
                                snackbarHostState.showSnackbar("✨ +$earned पॉइंट्स मिले! नई शायरी लोड हुई")
                            }
                        },
                        onCopy = {
                            val textToCopy = "${currentShayari.lines.joinToString("\n")}\n— ${currentShayari.author}\n(ओपन भारत ब्राउज़र से साभार)"
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Shayari", textToCopy))
                            Toast.makeText(context, "शायरी कॉपी हो गई! 📋", Toast.LENGTH_SHORT).show()
                        },
                        onShare = {
                            val textToShare = "✨ ${currentShayari.title} ✨\n\n${currentShayari.lines.joinToString("\n")}\n\n— ${currentShayari.author}\n\n🇮🇳 ओपन भारत ब्राउज़र पर पढ़ें और पाएं मुफ्त मोबाइल रिचार्ज!"
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, textToShare)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "शायरी शेयर करें"))
                        }
                    )

                    // 6. Reward Redeem Action Button (🎁 पॉइंट्स रिडीम करें और मोबाइल रिचार्ज जीतें!)
                    Spacer(modifier = Modifier.height(22.dp))
                    RedeemRewardBanner(
                        points = userPoints,
                        onClick = onOpenWallet
                    )
                }
            }
        }

        // Snackbar Host for Points Notifications
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}

@Composable
private fun HeaderBar(
    points: Int,
    onWalletClick: () -> Unit
) {
    // Header Bar matching original tricolor gradient: #FF9933, #FFFFFF, #138808
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFFF9933),
                        Color(0xFFFFFFFF),
                        Color(0xFF138808)
                    )
                )
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // App Title
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "🇮🇳 ओपन भारत",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF222222)
                )
            }

            // Wallet Container
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFFF9933)),
                shadowElevation = 3.dp,
                modifier = Modifier
                    .clickable { onWalletClick() }
                    .testTag("wallet_badge_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(text = "👛", fontSize = 17.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "$points Pts",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF222222)
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchContainer(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Search Input Box matching original border color #138808 and 30px pill radius
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = {
                Text(
                    text = "यहाँ गूगल सर्च करें...",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "सर्च",
                    tint = IndiaGreen
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(30.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch() }),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = IndiaGreen,
                unfocusedBorderColor = IndiaGreen,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .testTag("search_input_box")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Search Button with #138808 background
        Button(
            onClick = onSearch,
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = IndiaGreen,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(horizontal = 28.dp, vertical = 10.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
            modifier = Modifier.testTag("search_submit_button")
        ) {
            Text(
                text = "खोजें",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun AppsGrid(
    apps: List<QuickApp>,
    onAppClick: (QuickApp) -> Unit
) {
    // 5-column grid layout matching original index.html `.apps-grid`
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        val rows = apps.chunked(5)
        rows.forEach { rowApps ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                rowApps.forEach { app ->
                    AppItem(
                        app = app,
                        onClick = { onAppClick(app) }
                    )
                }
                // Fill remaining empty slots in row if less than 5
                val remaining = 5 - rowApps.size
                repeat(remaining) {
                    Spacer(modifier = Modifier.size(54.dp))
                }
            }
        }
    }
}

@Composable
private fun AppItem(
    app: QuickApp,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(4.dp)
            .testTag("app_item_${app.id}")
    ) {
        // Round app icon with distinctive top colored border
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .shadow(3.dp, CircleShape)
                .clip(CircleShape)
                .background(Color.White)
                .border(
                    width = 1.dp,
                    color = Color(0xFFDDDDDD),
                    shape = CircleShape
                )
        ) {
            // Top border accent matching original HTML
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .align(Alignment.TopCenter)
                    .background(Color(app.topBorderColorHex))
            )
            Text(
                text = app.iconEmoji,
                fontSize = 24.sp
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = app.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun ShayariCard(
    shayari: Shayari,
    onNext: () -> Unit,
    onCopy: () -> Unit,
    onShare: () -> Unit
) {
    // Card with left saffron border #FF9933 matching `.shayari-card`
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("shayari_card")
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Thick orange left border
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .height(130.dp)
                    .background(Color(0xFFFF9933))
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = shayari.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF9933)
                    )

                    IconButton(
                        onClick = onNext,
                        modifier = Modifier
                            .size(28.dp)
                            .testTag("next_shayari_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "अगली शायरी",
                            tint = Color(0xFFFF9933),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Column {
                    shayari.lines.forEach { line ->
                        Text(
                            text = "\"$line\"",
                            fontSize = 15.sp,
                            fontStyle = FontStyle.Italic,
                            lineHeight = 22.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "— ${shayari.author}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row {
                        IconButton(
                            onClick = onCopy,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "कॉपी करें",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        IconButton(
                            onClick = onShare,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "शेयर करें",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RedeemRewardBanner(
    points: Int,
    onClick: () -> Unit
) {
    // Reward Button matching `.reward-btn` with background #FF9933 and 30px pill radius
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFF9933),
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 14.dp),
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .testTag("redeem_rewards_banner_button")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🎁 पॉइंट्स रिडीम करें और मोबाइल रिचार्ज जीतें!",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}
