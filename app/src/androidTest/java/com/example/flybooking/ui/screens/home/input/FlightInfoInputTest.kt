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
class FlightInfoInputTest {

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
    fun testSelectDeparture() {
        val homeScreen = device.wait(Until.hasObject(By.desc("HOME_SCREEN")), TIMEOUT)
        assert(homeScreen) { "Home screen should be displayed by default" }

        sleep(1000)

        val locInput = device.findObject(By.desc("Departure"))
        assert(locInput != null) { "Departure field not found" }
        locInput.click()

        sleep(1000)

        val loc = device.findObject(By.text("Hanoi (HAN)"))
        assert(loc != null) { "Hanoi not found" }
        loc.click()

        sleep(1000)

        val depLoc = device.findObject(By.text("HAN"))
        assert(depLoc != null) { "HAN not found" }
    }

    @Test
    fun testSelectDestination() {
        val homeScreen = device.wait(Until.hasObject(By.desc("HOME_SCREEN")), TIMEOUT)
        assert(homeScreen) { "Home screen should be displayed by default" }

        sleep(1000)

        val locInput = device.findObject(By.desc("Destination"))
        assert(locInput != null) { "Destination field not found" }
        locInput.click()

        sleep(1000)

        val loc = device.findObject(By.text("Paris (PAR)"))
        assert(loc != null) { "Paris not found" }
        loc.click()

        sleep(500)

        val destLoc = device.findObject(By.text("PAR"))
        assert(destLoc != null) { "PAR not found" }
    }
}
