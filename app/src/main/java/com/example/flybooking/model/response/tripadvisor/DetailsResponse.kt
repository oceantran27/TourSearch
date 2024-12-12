package com.example.flybooking.model.response.tripadvisor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailsResponse(
    val name: String = "",
    @SerialName("address_obj") val addr: AddressObject = AddressObject(),
    val rating: String = "",
    @SerialName("rating_image_url") val ratingImageUrl: String = "",
    @SerialName("num_reviews") val numReviews: String = "",
    @SerialName("price_level") val priceLevel: String? = "",
    val latitude: String = "",
    val longitude: String = "",
    val amenities: List<String>? = emptyList()
)

@Serializable
data class AddressObject(
    val street1: String = "",
    val street2: String? = "",
    val city: String = "",
    val state: String? = "",
    val country: String = ""
) {
    override fun toString(): String {
        return "$street1${if (street2.isNullOrEmpty()) "" else ", $street2"}, $city${if (state.isNullOrEmpty()) "" else ", $state"}, $country"
    }
}
