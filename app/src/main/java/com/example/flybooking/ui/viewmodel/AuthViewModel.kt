package com.example.flybooking.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flybooking.firebase.AuthService
import com.example.flybooking.firebase.FirestoreService
import com.example.flybooking.model.Booking
import com.example.flybooking.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Authenticated : AuthState()
    object UnAuthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(
    private val authService: AuthService,
    private val firestoreService: FirestoreService
) : ViewModel() {
    private val _authState = MutableLiveData<AuthState>(AuthState.UnAuthenticated)
    val authState: LiveData<AuthState> = _authState
    private val _userStateFlow = MutableStateFlow<User?>(null)
    val userStateFlow: StateFlow<User?> get() = _userStateFlow

    private val _addBookingState = MutableLiveData<BookingState>(BookingState.Empty)
    val addBookingState: LiveData<BookingState> = _addBookingState

    init {
        updateAuthStatus()
    }

    fun getUserId(): String {
        return authService.getCurrentUserId() ?: ""
    }

    fun isLoggedIn(): Boolean {
        return authService.isLoggedIn()
    }

    fun updateAuthStatus() {
        if (authService.isLoggedIn()) {
            _authState.value = AuthState.Authenticated
            val userId = getUserId()
            viewModelScope.launch {
                dbReadUser(userId)
            }
        } else {
            _authState.value = AuthState.UnAuthenticated
        }
    }

    fun updateProfile(newUser: User) {
        viewModelScope.launch {
            try {
                val updatedUser = dbUpdateUser(newUser)
                if (updatedUser != null) {
                    _userStateFlow.value = updatedUser
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error updating user profile", e)
            }
        }
    }

    private suspend fun dbUpdateUser(user: User): User? {
        try {
            firestoreService.updateUser(user)
            return user
            Log.d("AuthViewModel", "User data saved successfully for user ID: ${user.id}")
        } catch (e: Exception) {
            return null
            Log.e("AuthViewModel", "Failed to save user data: ${e.message}", e)
        }
    }

    suspend fun dbCreateUser(user: User) {
        try {
            firestoreService.createUser(user)
            Log.d("AuthViewModel", "User data saved successfully for user ID: ${user.id}")
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Failed to save user data: ${e.message}", e)
        }
    }

    suspend fun dbReadUser(userId: String) {
        try {
            val user = firestoreService.readUser(userId)
            if (user != null) {
                _userStateFlow.value = user
            } else {
                Log.d("AuthViewModel", "User not found")
                _userStateFlow.value = null
            }
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Error fetching user data", e)
            _userStateFlow.value = null
        }
    }

    suspend fun login(email: String, password: String) {
        _authState.value = AuthState.Loading
        try {
            if (email.isEmpty() || password.isEmpty()) {
                _authState.value = AuthState.Error("Email or password cannot be empty")
            } else {
                val success = authService.signIn(email, password)
                if (success) {
                    _authState.value = AuthState.Authenticated
                    val userId = getUserId()
                    dbReadUser(userId)
                } else {
                    _authState.value = AuthState.Error("Login failed")
                }
            }
        } catch (e: Exception) {
            _authState.value = AuthState.Error(e.message ?: "Login failed")
        }
    }

    suspend fun signUp(email: String, password: String) {
        _authState.value = AuthState.Loading
        try {
            if (email.isEmpty() || password.isEmpty()) {
                _authState.value = AuthState.Error("Email or password cannot be empty")
            } else if (password.length < 6) {
                _authState.value = AuthState.Error("Password should be at least 6 characters")
            } else {
                val success = authService.signUp(email, password)
                if (success) {
                    _authState.value = AuthState.Authenticated
                    val userId = getUserId()
                    dbReadUser(userId)
                } else {
                    _authState.value = AuthState.Error("SignUp failed")
                }
            }
        } catch (e: Exception) {
            _authState.value = AuthState.Error(e.message ?: "SignUp failed")
        }
    }

    suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ): Result<Unit> {
        if (newPassword != confirmPassword) {
            return Result.failure(Exception("New password and confirm password do not match"))
        }

        if (newPassword.length < 6) {
            return Result.failure(Exception("Password should be at least 6 characters"))
        }

        return authService.changePassword(currentPassword, newPassword)
    }

    fun signOut() {
        _authState.value = AuthState.Loading
        authService.signOut()
        _authState.value = AuthState.UnAuthenticated
    }

    suspend fun addHistoryBooking(booking: Booking) {
        _addBookingState.value = BookingState.Loading
        val userId = getUserId()
        if (userId.isNotEmpty()) {
            try {
                val bookingId = firestoreService.createBooking(booking)

                val user = firestoreService.readUser(userId)
                if (user != null) {
                    val updatedBookingHistory = user.bookingHistory.toMutableList().apply {
                        add(bookingId)
                    }

                    val updatedUser = user.copy(bookingHistory = updatedBookingHistory)
                    firestoreService.updateUser(updatedUser)
                    Log.d("AuthViewModel", "Booking added to history successfully")
                    _addBookingState.value =
                        BookingState.Success("Booking added to history successfully")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error adding booking to history", e)
                _addBookingState.value = BookingState.Error(e.message ?: "Error adding booking")
            }
        }
    }
}
