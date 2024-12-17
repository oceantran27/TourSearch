package com.example.flybooking.ui.screens.home.input

import android.content.Context
import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.example.flybooking.navigation.AppScreens
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep

@RunWith(AndroidJUnit4::class)
class PassengerInfoTest {

    private lateinit var device: UiDevice
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val PACKAGE_NAME = "com.example.flybooking"
    private val TIMEOUT = 30000L

    @Before
    fun setUp() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val intent = context.packageManager.getLaunchIntentForPackage(PACKAGE_NAME)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
        device.wait(Until.hasObject(By.pkg(PACKAGE_NAME).depth(0)), TIMEOUT)
    }

    @Test
    fun addTwoAdults() {
        val homeScreen = device.wait(Until.hasObject(By.desc(AppScreens.Home.name)), TIMEOUT)
        assert(homeScreen) { "Home screen should be displayed by default" }

        val adultField = device.findObject(By.desc("Adults"))
        assert(adultField != null) { "Adults field not found" }
        val addButton = adultField.findObject(By.text("+"))
        assert(addButton != null) { "Add button not found" }
        val adultText = device.findObject(By.desc("Adults text"))
        assert(adultText != null) { "Adult text not found" }

        addButton.click()
        sleep(5000)
        addButton.click()
        sleep(5000)

        val adultCount = adultText.text
        println("Adult count after clicks: $adultCount")
        assert(adultCount == "3 Adult") { "Expected: 3 Adult, Found: $adultCount" }
    }

    @Test
    fun addTwoChildren() {
        val homeScreen = device.wait(Until.hasObject(By.desc(AppScreens.Home.name)), TIMEOUT)
        assert(homeScreen) { "Home screen should be displayed by default" }

        val childrenField = device.findObject(By.desc("Children"))
        assert(childrenField != null) { "Children field not found" }
        val addButton = childrenField.findObject(By.text("+"))
        assert(addButton != null) { "Add button not found" }
        val childrenText = device.findObject(By.desc("Children text"))
        assert(childrenText != null) { "Children text not found" }

        addButton.click()
        sleep(5000)
        addButton.click()
        sleep(5000)

        val childrenCount = childrenText.text
        println("Children count after clicks: $childrenCount")
        assert(childrenCount == "3 Child") { "Expected: 3 Child, Found: $childrenCount" }
    }

    @Test
    fun removeOneChild() {
        val homeScreen = device.wait(Until.hasObject(By.desc(AppScreens.Home.name)), TIMEOUT)
        assert(homeScreen) { "Home screen should be displayed by default" }

        val childrenField = device.findObject(By.desc("Children"))
        assert(childrenField != null) { "Children field not found" }
        val removeButton = childrenField.findObject(By.text("-"))
        assert(removeButton != null) { "Remove button not found" }
        val childrenText = device.findObject(By.desc("Children text"))
        assert(childrenText != null) { "Children text not found" }

        removeButton.click()
        sleep(5000)

        val childrenCount = childrenText.text
        println("Children count after click: $childrenCount")
        assert(childrenCount == "0 Child") { "Expected: 0 Child, Found: $childrenCount" }
    }
}
