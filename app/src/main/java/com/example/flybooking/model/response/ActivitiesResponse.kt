package com.example.flybooking.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ActivitiesResponse(
    val data: List<Activity>
)

@Serializable
data class Activity(
    val name: String? = null,
    val description: String? = null,
    val geoCode: GeoCode? = null,
    val price: ActivityPrice? = null,
    val pictures: List<String>? = null
){
    fun isValid(): Boolean {
        return !name.isNullOrEmpty() &&
                !description.isNullOrEmpty() &&
                geoCode != null &&
                price?.isValid() == true &&
                pictures?.isNotEmpty() == true
    }
}

@Serializable
data class ActivityPrice(
    val amount: String? = null,
    val currencyCode: String? = null
){
    override fun toString(): String {
        return "$amount $currencyCode"
    }

    fun isValid(): Boolean {
        return amount?.toDoubleOrNull()?.let { amount -> amount > 0 } == true &&
                currencyCode != null
    }
}


class ActivityCard (
    val activity: Activity,
    val selected: Boolean
)
