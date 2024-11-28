package com.example.flybooking.navigation

import android.content.Context
import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

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
        device.wait(Until.hasObject(By.pkg(PACKAGE_NAME).depth(0)), 5000)
    }

    @Test
    fun testHomeScreenDisplayedByDefault() {
        // Check if the Home screen is displayed by default
        val homeScreen = device.findObject(By.desc("Screen_Home"))
        assertNotNull("Home screen should be displayed by default", homeScreen)
    }

    @Test
    fun testNavigateToExploreScreen() {
        // Click on the Explore button
        val exploreButton = device.findObject(By.desc("Explore"))
        exploreButton.click()

        // Wait for the Explore screen to appear
        val timeout = CHANGE_SCREEN_TIMEOUT
        val exploreScreen = device.wait(Until.hasObject(By.desc("Screen_Explore")), timeout)

        // Check if the Profile screen is displayed
        assert(exploreScreen) {
            "Explore screen should be displayed after clicking Explore"
        }
    }

    @Test
    fun testNavigateToBookmarksScreen() {
        // Click on the Bookmarks button
        val bookmarksButton = device.findObject(By.desc("Bookmarks"))
        bookmarksButton.click()

        // Wait for the Bookmarks screen to appear
        val timeout = CHANGE_SCREEN_TIMEOUT
        val bookmarksScreen = device.wait(Until.hasObject(By.desc("Screen_Bookmarks")), timeout)

        // Check if the Bookmarks screen is displayed
        assert(bookmarksScreen) {
            "Bookmarks screen should be displayed after clicking Bookmarks"
        }
    }

    @Test
    fun testNavigateToProfileScreen() {
        // Click on the Profile button
        val profileButton = device.findObject(By.desc("Profile"))
        profileButton.click()

        // Wait for the Profile screen to appear
        val timeout = CHANGE_SCREEN_TIMEOUT
        val profileScreen = device.wait(Until.hasObject(By.desc("Screen_Profile")), timeout)

        // Check if the Profile screen is displayed
        assert(profileScreen) {
            "Profile screen should be displayed after clicking Profile"
        }
    }
}
