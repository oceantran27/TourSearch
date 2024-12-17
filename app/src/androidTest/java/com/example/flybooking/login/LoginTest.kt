package com.example.flybooking.login

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
class LoginTest {
    private val TIMEOUT = 5000L
    private lateinit var device: UiDevice
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val PACKAGE_NAME = "com.example.flybooking"

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
    fun testOpenLoginScreen() {
        val homeScreen = device.wait(Until.hasObject(By.desc(AppScreens.Home.name)), 10000L)
        assert(homeScreen) {
            "Home screen should be displayed by default"
        }

        sleep(500)

        var loginPageButton = device.findObject(By.desc("Account_Button"))
        assert(loginPageButton != null) {
            "Login page button should be displayed"
        }

        loginPageButton.click()
        sleep(2000)

        val signOutButton = device.findObject(By.desc("SignOutButton"))
        if (signOutButton != null) {
            signOutButton.click()
            sleep(500)
            loginPageButton = device.wait(Until.findObject(By.desc("Account_Button")), 10000L)
            assert(loginPageButton != null) { "Account button should reappear after app restart." }
            loginPageButton.click()
            sleep(500)
        }

        val loginScreen = device.wait(Until.hasObject(By.desc("LoginScreen")), 10000L)
        assert(loginScreen) {
            "Login screen should be displayed"
        }
    }

    @Test
    fun testLogin() {
        var homeScreen = device.wait(Until.hasObject(By.desc(AppScreens.Home.name)), 10000L)
        assert(homeScreen) {
            "Home screen should be displayed by default"
        }

        sleep(500)

        var loginPageButton = device.findObject(By.desc("Account_Button"))

        loginPageButton.click()
        sleep(2000)

        val signOutButton = device.findObject(By.desc("SignOutButton"))
        if (signOutButton != null) {
            signOutButton.click()
            sleep(500)
            loginPageButton = device.wait(Until.findObject(By.desc("Account_Button")), 10000L)
            assert(loginPageButton != null) { "Account button should reappear after app restart." }
            loginPageButton.click()
            sleep(500)
        }

        val loginScreen = device.wait(Until.hasObject(By.desc("LoginScreen")), 10000L)
        assert(loginScreen) {
            "Login screen should be displayed"
        }

        val emailInput = device.findObject(By.desc("Email_Input"))
        assert(emailInput != null) {
            "Email input field should be displayed"
        }
        emailInput.click()
        sleep(500)
        device.executeShellCommand("input text admin@gmail.com")

        val passwordInput = device.findObject(By.desc("Password_Input"))
        assert(passwordInput != null) {
            "Password input field should be displayed"
        }
        val loginButton = device.findObject(By.desc("Login_Button")) ?: device.findObject(By.text("Login"))
        assert(loginButton != null) {
            "Login button should be displayed"
        }
        passwordInput.click()
        sleep(500)
        device.executeShellCommand("input text 123456789")
        device.executeShellCommand("input keyevent KEYCODE_BACK")
        sleep(500)

        loginButton.click()

        sleep(3000)

        homeScreen = device.wait(Until.hasObject(By.desc(AppScreens.Home.name)), 10000L)
        assert(homeScreen) {
            "Home screen should be displayed"
        }
    }

    @Test
    fun testWrongPassword() {
        var homeScreen = device.wait(Until.hasObject(By.desc(AppScreens.Home.name)), 10000L)
        assert(homeScreen) {
            "Home screen should be displayed by default"
        }

        sleep(500)

        var loginPageButton = device.findObject(By.desc("Account_Button"))
        assert(loginPageButton != null) {
            "Login page button should be displayed"
        }

        loginPageButton.click()
        sleep(2000)

        val signOutButton = device.findObject(By.desc("SignOutButton"))
        if (signOutButton != null) {
            signOutButton.click()
            sleep(500)
            loginPageButton = device.wait(Until.findObject(By.desc("Account_Button")), 500L)
            assert(loginPageButton != null) { "Account button should reappear after app restart." }
            loginPageButton.click()
            sleep(500)
        }

        val loginScreen = device.wait(Until.hasObject(By.desc("LoginScreen")), 10000L)
        assert(loginScreen) {
            "Login screen should be displayed"
        }

        val emailInput = device.findObject(By.desc("Email_Input"))
        assert(emailInput != null) {
            "Email input field should be displayed"
        }
        emailInput.click()
        sleep(500)
        device.executeShellCommand("input text admin")

        val passwordInput = device.findObject(By.desc("Password_Input"))
        assert(passwordInput != null) {
            "Password input field should be displayed"
        }
        val loginButton = device.findObject(By.desc("Login_Button")) ?: device.findObject(By.text("Login"))
        assert(loginButton != null) {
            "Login button should be displayed"
        }
        passwordInput.click()
        sleep(500)
        device.executeShellCommand("input text 123")
        device.executeShellCommand("input keyevent KEYCODE_BACK")
        sleep(500)
        loginButton.click()

        sleep(3000)

        homeScreen = device.wait(Until.hasObject(By.desc(AppScreens.Home.name)), 1000L)
        assert(!homeScreen) {
            "Home screen should not be displayed"
        }
    }
}