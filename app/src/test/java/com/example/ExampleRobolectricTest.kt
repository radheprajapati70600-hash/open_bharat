package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.ShayariRepository
import com.example.data.WalletRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read app name from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Open Bharat", appName)
    }

    @Test
    fun `wallet points earned for search`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val walletRepo = WalletRepository(context)
        val initialPoints = walletRepo.points.value
        val earned = walletRepo.addPointsForSearch()
        assertEquals(10, earned)
        assertEquals(initialPoints + 10, walletRepo.points.value)
    }

    @Test
    fun `shayari repository has content`() {
        assertTrue(ShayariRepository.shayaris.isNotEmpty())
        val first = ShayariRepository.shayaris[0]
        assertTrue(first.lines.isNotEmpty())
    }
}
