package com.example.flybooking.firebase

interface AuthService {
    suspend fun signIn(email: String, password: String): Boolean
    suspend fun signUp(email: String, password: String): Boolean
    suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit>
    fun signOut()
    fun getCurrentUserId(): String?
    fun isLoggedIn(): Boolean
}