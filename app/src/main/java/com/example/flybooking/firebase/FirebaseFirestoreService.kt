package com.example.flybooking.firebase

import com.example.flybooking.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseFirestoreService : FirestoreService {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override suspend fun createUser(user: User) {
        firestore.collection("users").document(user.id).set(user).await()
    }

    override suspend fun readUser(userId: String): User? {
        val document = firestore.collection("users").document(userId).get().await()
        return if (document.exists()) document.toObject(User::class.java) else null
    }

    override suspend fun updateUser(user: User) {
        firestore.collection("users").document(user.id).set(user).await()
    }
}