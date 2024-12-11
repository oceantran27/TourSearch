package com.example.flybooking.firebase

import com.example.flybooking.model.Booking
import com.example.flybooking.model.User

interface FirestoreService {
    //CRUD user
    suspend fun createUser(user: User)
    suspend fun readUser(userId: String): User?
    suspend fun updateUser(user: User)

    //CRUD bookings
    suspend fun createBooking(booking: Booking): String
    suspend fun readBooking(bookingId: String): Booking?
    suspend fun updateBooking(booking: Booking)
    suspend fun deleteBooking(bookingId: String)
}