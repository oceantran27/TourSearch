package com.example.flybooking.model.response.amadeus

import kotlinx.serialization.Serializable

@Serializable
data class AccessTokenResponse(
    val access_token: String,
    val expires_in: Long,
    val token_type: String
)
