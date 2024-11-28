package com.example.flybooking

import com.example.flybooking.firebase.FirebaseAuthService
import com.example.flybooking.firebase.FirestoreService
import com.example.flybooking.ui.viewmodel.AuthState
import com.example.flybooking.ui.viewmodel.AuthViewModel
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock

class AuthViewModelTest {

    private lateinit var authViewModel: AuthViewModel
    private val mockAuthService = mock<FirebaseAuthService>()
    private val mockFirestoreService = mock<FirestoreService>()

    @Before
    fun setup() {
        authViewModel = AuthViewModel(mockAuthService, mockFirestoreService)
    }

    @Test
    fun `test login success`() = runBlocking {
        // Arrange
        val email = "test@example.com"
        val password = "password123"
        `when`(mockAuthService.signIn(email, password)).thenReturn(true)

        // Act
        authViewModel.login(email, password)

        // Assert
        assertTrue(authViewModel.authState.value is AuthState.Authenticated)
    }

    @Test
    fun `test login failure`() = runBlocking {
        // Arrange
        val email = "test@example.com"
        val password = "wrongpassword"
        `when`(mockAuthService.signIn(email, password)).thenReturn(false)

        // Act
        authViewModel.login(email, password)

        // Assert
        assertTrue(authViewModel.authState.value is AuthState.Error)
    }
}
