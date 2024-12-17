package com.example.flybooking.navigation

import android.content.Context
import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep

@RunWith(AndroidJUnit4::class)
class AppNavigationTest {

    private lateinit var device: UiDevice
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val PACKAGE_NAME = "com.example.flybooking"
    private val CHANGE_SCREEN_TIMEOUT = 2000L

    @Before
    fun setUp() {
        // Initialize UiDevice instance
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        // Launch the app
        val intent = context.packageManager.getLaunchIntentForPackage(PACKAGE_NAME)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) // Clear out any previous instances
        context.startActivity(intent)

        // Wait for the app to appear
        device.wait(Until.hasObject(By.pkg(PACKAGE_NAME).depth(0)), 50000)
    }

    @Test
    fun testHomeScreenDisplayedByDefault() {
        val homeScreen = device.wait(Until.hasObject(By.desc(AppScreens.Home.name)), 10000L)
        assert(homeScreen) {
            "Home screen should be displayed by default"
        }
    }

    @Test
    fun testBookmarkButtonAvailable() {
        val homeScreen = device.wait(Until.hasObject(By.desc(AppScreens.Home.name)), 10000L)
        assert(homeScreen) {
            "Home screen should be displayed by default"
        }
        sleep(300)
        val bookmarkButton = device.findObject(By.desc(AppScreens.Bookmark.name + "_TAB"))
        assert(bookmarkButton != null) {
            "Bookmark button not found"
        }

        bookmarkButton.click()
        sleep(500)

        val bookmarkScreen = device.wait(Until.hasObject(By.desc(AppScreens.Bookmark.name)), 10000L)
        assert(bookmarkScreen) {
            "Bookmark screen should be displayed"
        }
    }
}
