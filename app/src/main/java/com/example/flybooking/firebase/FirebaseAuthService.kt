package com.example.flybooking.firebase

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseAuthService : AuthService {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun signIn(email: String, password: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun signUp(email: String, password: String): Boolean {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun changePassword(
        currentPassword: String,
        newPassword: String
    ): Result<Unit> {
        val user = auth.currentUser ?: return Result.failure(Exception("User not logged in"))
        val credential = EmailAuthProvider.getCredential(user.email ?: "", currentPassword)

        return suspendCoroutine { cont ->
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
    }

    override fun signOut() {
        auth.signOut()
    }

    override fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    override fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}