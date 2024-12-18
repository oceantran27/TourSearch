package com.example.flybooking.ui.screens.login

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
//        device.executeShellCommand("input keyevent KEYCODE_BACK")
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
//        device.executeShellCommand("input keyevent KEYCODE_BACK")
        sleep(500)
        loginButton.click()

        sleep(3000)

        homeScreen = device.wait(Until.hasObject(By.desc(AppScreens.Home.name)), 1000L)
        assert(!homeScreen) {
            "Home screen should not be displayed"
        }
    }

    @Test
    fun testRegister() {
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

        sleep(500)

        val registerButton = device.findObject(By.desc("SignUpPageButton"))
        assert(registerButton != null) {
            "Sign up page button should be displayed"
        }
        registerButton.click()
        sleep(500)
        val fullNameField = device.findObject(By.desc("Full name input"))
        assert(fullNameField != null) {
            "Full name input field should be displayed"
        }
        val validEmailField = device.findObject(By.desc("Email input"))
        assert(validEmailField != null) {
            "Email input field should be displayed"
        }
        val phoneNumberField = device.findObject(By.desc("Phone number input"))
        assert(phoneNumberField != null) {
            "Phone number input field should be displayed"
        }
        val passwordField = device.findObject(By.desc("Password input"))
        assert(passwordField != null) {
            "Password input field should be displayed"
        }
        val signUpButton = device.findObject(By.desc("SignUpButton"))
        assert(signUpButton != null) {
            "Sign up button should be displayed"
        }

        fullNameField.click()
        sleep(500)
        device.executeShellCommand("input text someone")
        // Generate a random email
        val email = "someone" + (10..999999).random() + "@gmail.com"
        validEmailField.click()
        sleep(500)
        device.executeShellCommand("input text $email")
        phoneNumberField.click()
        sleep(500)
        device.executeShellCommand("input text 123456789")
        passwordField.click()
        sleep(500)
        device.executeShellCommand("input text 123456789")

        signUpButton.click()
        sleep(3000)

        homeScreen = device.wait(Until.hasObject(By.desc(AppScreens.Home.name)), 10000L)
        assert(homeScreen) {
            "Home screen should be displayed"
        }
    }

    @Test
    fun testChangeAccountInfo() {
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
        sleep(500)

        val signOutButton = device.findObject(By.desc("SignOutButton"))
        if (signOutButton == null) {
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
            sleep(500)

            loginButton.click()
            sleep(3000)

            loginPageButton = device.findObject(By.desc("Account_Button"))
            assert(loginPageButton != null) {
                "Login page button should be displayed"
            }

            loginPageButton.click()
            sleep(500)
        }

        val editProfileButton = device.findObject(By.desc("EditProfileButton"))
        assert(editProfileButton != null) {
            "Edit profile button should be displayed"
        }
        editProfileButton.click()
        sleep(500)

        val phoneField = device.findObject(By.desc("PhoneEditField"))
        assert(phoneField != null) {
            "Phone edit field should be displayed"
        }
        val randomPhone = (100000000..999999999).random().toString()
        phoneField.click()
        sleep(100)
        // Select all text and delete, then input new phone number
        val phoneLength = phoneField.text.length
        for (i in 0 until phoneLength) {
            device.pressDelete()
        }
        sleep(100)
        device.executeShellCommand("input text $randomPhone")
        sleep(500)

        val saveButton = device.findObject(By.desc("SaveChangesButton"))
        assert(saveButton != null) {
            "Save changes button should be displayed"
        }
        saveButton.click()
        sleep(1500)

        val phoneFieldAfterSave = device.findObject(By.desc("PhoneField"))
        assert(phoneFieldAfterSave != null) {
            "Phone field should be displayed"
        }
        assert(phoneFieldAfterSave.text == randomPhone) {
            "Phone number should be updated"
        }
    }
}