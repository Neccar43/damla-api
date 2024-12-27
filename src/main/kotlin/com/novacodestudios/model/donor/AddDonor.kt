package com.novacodestudios.model.donor

import kotlinx.serialization.Serializable

@Serializable
data class AddDonor(
    val name: String,
    val email: String,
    val password: String,
    val phone: String,
    val bloodGroup: String,
    val lastDonationDate: String? = null,
)