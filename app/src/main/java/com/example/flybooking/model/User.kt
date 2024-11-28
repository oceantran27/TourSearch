package com.example.flybooking.model

data class User(
    val id: String,
    val fullName: String? = null,
    val email: String,
    val phoneNumber: String? = null,
    val avatarUri: String? = null,
    val birthday: Long = 0L,
) {
    constructor() : this("null", null, "null@null.com", null, null, 0L)
}
