package com.example.flybooking.firebase

import android.util.Log
import com.example.flybooking.model.Booking
import com.example.flybooking.model.User
import com.example.flybooking.ui.viewmodel.serializeBooking
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class FirebaseFirestoreService : FirestoreService {
    private val db = FirebaseFirestore.getInstance()

    //CRUD user
    override suspend fun createUser(user: User) {
        db.collection("users").document(user.id).set(user).await()
    }

    override suspend fun readUser(userId: String): User? {
        val document = db.collection("users").document(userId).get().await()
        return if (document.exists()) document.toObject(User::class.java) else null
    }

    override suspend fun updateUser(user: User) {
        db.collection("users").document(user.id).set(user, SetOptions.merge()).await()
    }

    //CRUD bookings
    override suspend fun createBooking(booking: Booking): String {
        val bookingRef = db.collection("bookings").document()
        val bookingId = bookingRef.id

        val bookingData = booking.copy(id = bookingId)
        val bookingJson = serializeBooking(bookingData)
        Log.d("FirebaseFirestoreService", "Serialized Booking: $bookingJson")

        bookingRef.set(bookingData, SetOptions.merge()).await()

        return bookingId
    }

    override suspend fun readBooking(bookingId: String): Booking? {
        val bookingRef = db.collection("bookings").document(bookingId)
        val bookingSnapshot = bookingRef.get().await()
        return bookingSnapshot.toObject(Booking::class.java)
    }

    override suspend fun updateBooking(booking: Booking) {
        val bookingRef = db.collection("bookings").document(booking.id)
        bookingRef.set(booking, SetOptions.merge()).await()
    }

    override suspend fun deleteBooking(bookingId: String) {
        val bookingRef = db.collection("bookings").document(bookingId)
        bookingRef.delete().await()
    }
}
