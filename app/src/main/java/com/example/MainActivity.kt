package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.data.WalletRepository
import com.example.ui.BrowserScreen
import com.example.ui.HomeScreen
import com.example.ui.WalletBottomSheet
import com.example.ui.theme.OpenBharatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val walletRepository = WalletRepository(applicationContext)

        setContent {
            OpenBharatTheme {
                MainAppRoot(walletRepository = walletRepository)
            }
        }
    }
}

@Composable
fun MainAppRoot(walletRepository: WalletRepository) {
    var activeBrowserUrl by remember { mutableStateOf<String?>(null) }
    var isWalletOpen by remember { mutableStateOf(false) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        if (activeBrowserUrl != null) {
            BrowserScreen(
                initialUrl = activeBrowserUrl!!,
                onCloseBrowser = { activeBrowserUrl = null },
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            HomeScreen(
                walletRepository = walletRepository,
                onNavigateToUrl = { url ->
                    activeBrowserUrl = url
                },
                onOpenWallet = {
                    isWalletOpen = true
                },
                modifier = Modifier.padding(innerPadding)
            )
        }

        if (isWalletOpen) {
            WalletBottomSheet(
                walletRepository = walletRepository,
                onDismiss = { isWalletOpen = false }
            )
        }
    }
}
