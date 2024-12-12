package com.example.flybooking.model

data class User(
    var id: String,
    var fullName: String? = null,
    var email: String,
    var phoneNumber: String? = null,
    var avatarUri: String? = null,
    var birthday: Long = 0L,
    var bookingHistory: List<String> = emptyList()
) {
    constructor() : this("null", null, "null@null.com", null, null, 0L, emptyList())
}
