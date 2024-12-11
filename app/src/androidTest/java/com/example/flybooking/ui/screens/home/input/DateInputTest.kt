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
class DateInputTest {

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
    fun testDepartDateSelectionUpdatesHomeViewModel() {
        val homeScreen = device.wait(Until.hasObject(By.desc("HOME_SCREEN")), TIMEOUT)
        assert(homeScreen) { "Home screen should be displayed by default" }

        sleep(1000)

        val dateInput = device.findObject(By.desc("Departure text"))
        assert(dateInput != null) { "Departure text field not found" }
        dateInput.click()

        sleep(1000)

        val day = device.findObject(By.text("29"))
        assert(day != null) { "Date '29' not found in DatePicker" }
        day.click()

        val okButton = device.findObject(By.text("OK"))
        assert(okButton != null) { "OK button not found in DatePicker" }
        okButton.click()

        sleep(500)

        val selectedDateField = device.findObject(By.desc("Departure text"))
        assert(selectedDateField != null) { "Departure text field not found" }
        val selectedDateText = selectedDateField.text
        assert(selectedDateText == "29 Dec, 2024") {
            "Expected: 29 Dec, 2024, Found: $selectedDateText"
        }
    }

    @Test
    fun testReturnDateSelectionUpdatesHomeViewModel() {
        val homeScreen = device.wait(Until.hasObject(By.desc("HOME_SCREEN")), TIMEOUT)
        assert(homeScreen) { "Home screen should be displayed by default" }

        sleep(1000)

        val dateInput = device.findObject(By.desc("Return text"))
        assert(dateInput != null) { "Return text field not found" }
        dateInput.click()

        sleep(1000)

        val day = device.findObject(By.text("30"))
        assert(day != null) { "Date '30' not found in DatePicker" }
        day.click()

        val okButton = device.findObject(By.text("OK"))
        assert(okButton != null) { "OK button not found in DatePicker" }
        okButton.click()

        sleep(500)

        val selectedDateField = device.findObject(By.desc("Return text"))
        assert(selectedDateField != null) { "Return text field not found" }
        val selectedDateText = selectedDateField.text
        assert(selectedDateText == "30 Dec, 2024") {
            "Expected: 30 Dec, 2024, Found: $selectedDateText"
        }
    }
}
