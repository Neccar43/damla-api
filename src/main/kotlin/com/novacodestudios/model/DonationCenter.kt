package com.novacodestudios.model

import kotlinx.serialization.Serializable

@Serializable
data class AddDonationCenter(
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val openingTime: String,
    val closingTime: String,
)

@Serializable
data class UpdateDonationCenter(
    val id: Int,
    val name: String? = null,
    val address: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val openingTime: String? = null,
    val closingTime: String? = null,
)
