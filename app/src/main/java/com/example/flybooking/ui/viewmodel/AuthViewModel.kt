package com.example.flybooking.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flybooking.model.User
import com.google.firebase.Timestamp
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

sealed class AuthState {
    object Authenticated : AuthState()
    object UnAuthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _authState = MutableLiveData<AuthState>(AuthState.UnAuthenticated)
    val authState: LiveData<AuthState> = _authState
    val firestore = FirebaseFirestore.getInstance()
    private val _userStateFlow = MutableStateFlow<User?>(null)
    val userStateFlow: StateFlow<User?> get() = _userStateFlow

    init {
        updateAuthStatus()
    }

    fun getUserId(): String {
        return auth.currentUser?.uid ?: ""
    }

    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun updateAuthStatus() {
        if (auth.currentUser == null) {
            _authState.value = AuthState.UnAuthenticated
        } else {
            _authState.value = AuthState.Authenticated
            val userId = getUserId()
            viewModelScope.launch {
                dbReadUser(userId)
            }
        }
    }

    suspend fun dbCreateUser(user: User) {
        try {
            firestore.collection("users").document(user.id).set(user).await()
            Log.d("AuthViewModel", "User data saved successfully for user ID: ${user.id}")
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Failed to save user data: ${e.message}", e)
        }
    }

    suspend fun dbReadUser(userId: String) {
        try {
            val document = firestore.collection("users").document(userId).get().await()
            if (document.exists()) {
                val user = document.toObject(User::class.java)
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

    fun updateProfile(newUser: User) {
        viewModelScope.launch {
            try {
                val updatedUser = dbUpdateUser(newUser)
                _userStateFlow.value = updatedUser
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error updating user profile", e)
            }
        }
    }

    private suspend fun dbUpdateUser(user: User): User {
        val userRef = FirebaseFirestore.getInstance().collection("users").document(user.id)
        userRef.set(user).await()
        return user
    }

    suspend fun login(email: String, password: String) {
        _authState.value = AuthState.Loading
        try {
            if (email.isEmpty() || password.isEmpty()) {
                _authState.value = AuthState.Error("Email or password cannot be empty")
            } else {
                auth.signInWithEmailAndPassword(email, password).await()
                _authState.value = AuthState.Authenticated
                val userId = getUserId()
                dbReadUser(userId)
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
                auth.createUserWithEmailAndPassword(email, password).await()
                _authState.value = AuthState.Authenticated
                val userId = getUserId()
                dbReadUser(userId)
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

        val user = auth.currentUser ?: return Result.failure(Exception("User not logged in"))

        val credential = EmailAuthProvider.getCredential(user.email ?: "", currentPassword)

        try {
            val reauthResult = suspendCoroutine<Result<Unit>> { cont ->
                user.reauthenticate(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        user.updatePassword(newPassword).addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                cont.resume(Result.success(Unit))
                            } else {
                                cont.resume(
                                    Result.failure(
                                        updateTask.exception ?: Exception("Password update failed")
                                    )
                                )
                            }
                        }
                    } else {
                        cont.resume(
                            Result.failure(
                                task.exception ?: Exception("Incorrect current password")
                            )
                        )
                    }
                }
            }

            return reauthResult
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }


    fun signOut() {
        _authState.value = AuthState.Loading
        auth.signOut()
        _authState.value = AuthState.UnAuthenticated
    }
}