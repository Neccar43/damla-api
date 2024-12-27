package com.novacodestudios.model

import kotlinx.serialization.Serializable

@Serializable
data class AddDonation(
    val appointmentId: Int,
    val personnelId: Int,
    val successful: Boolean,
    val donationDate: String,
    val remarks: String,
)

@Serializable
data class UpdateDonation(
    val id: Int,
    val appointmentId: Int? = null,
    val personnelId: Int? = null,
    val successful: Boolean? = null,
    val donationDate: String? = null,
    val remarks: String? = null,
)
