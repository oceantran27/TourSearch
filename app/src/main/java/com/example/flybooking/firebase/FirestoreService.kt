package com.example.flybooking.firebase

import com.example.flybooking.model.User

interface FirestoreService {
    suspend fun createUser(user: User)
    suspend fun readUser(userId: String): User?
    suspend fun updateUser(user: User)
}