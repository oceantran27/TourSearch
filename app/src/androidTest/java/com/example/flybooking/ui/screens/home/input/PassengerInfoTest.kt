package com.example.flybooking.ui.screens.home.input

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
class PassengerInfoTest {

    private lateinit var device: UiDevice
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val PACKAGE_NAME = "com.example.flybooking"
    private val TIMEOUT = 5000L

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
        val homeScreen = device.wait(Until.hasObject(By.desc("HOME_SCREEN")), TIMEOUT)
        assert(homeScreen) { "Home screen should be displayed by default" }

        val adultField = device.findObject(By.desc("Adults"))
        assert(adultField != null) { "Adults field not found" }
        val addButton = adultField.findObject(By.text("+"))
        assert(addButton != null) { "Add button not found" }

        addButton.click()
        sleep(1000)
        addButton.click()
        sleep(1000)

        val adultCount = device.findObject(By.text("3 Adult"))
        assert(adultCount != null) { "Adult count not found" }
    }

    @Test
    fun addThreeChildren() {
        val homeScreen = device.wait(Until.hasObject(By.desc("HOME_SCREEN")), TIMEOUT)
        assert(homeScreen) { "Home screen should be displayed by default" }

        val childrenField = device.findObject(By.desc("Children"))
        assert(childrenField != null) { "Children field not found" }
        val addButton = childrenField.findObject(By.text("+"))
        assert(addButton != null) { "Add button not found" }

        addButton.click()
        sleep(1000)
        addButton.click()
        sleep(1000)
        addButton.click()
        sleep(1000)

        val childrenCount = device.findObject(By.text("4 Child"))
        assert(childrenCount != null) { "Children count not found" }
    }

    @Test
    fun removeOneChild() {
        val homeScreen = device.wait(Until.hasObject(By.desc("HOME_SCREEN")), TIMEOUT)
        assert(homeScreen) { "Home screen should be displayed by default" }

        val childrenField = device.findObject(By.desc("Children"))
        assert(childrenField != null) { "Children field not found" }

        val removeButton = childrenField.findObject(By.text("-"))
        assert(removeButton != null) { "Remove button not found" }

        removeButton.click()
        sleep(1000)
        val childrenCount = device.findObject(By.text("0 Child"))
        assert(childrenCount != null) { "Children count not found" }
    }
}