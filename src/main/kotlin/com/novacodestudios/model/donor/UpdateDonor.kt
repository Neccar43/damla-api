package com.novacodestudios.model.donor

import kotlinx.serialization.Serializable

@Serializable
data class UpdateDonor(
    val id: Int,
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val phone: String? = null,
    val bloodGroup: String? = null,
    val lastDonationDate: String? = null,
)