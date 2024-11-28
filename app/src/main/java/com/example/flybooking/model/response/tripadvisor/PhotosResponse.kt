package com.example.flybooking.model.response.tripadvisor

import kotlinx.serialization.Serializable

@Serializable
data class PhotosResponse(
    val data: List<Photo>
)

@Serializable
data class Photo(
    val images: Image
)

@Serializable
data class Image(
    val large: Large
)

@Serializable
data class Large(
    val url: String
)

