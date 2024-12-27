package com.novacodestudios.model.personnel

import kotlinx.serialization.Serializable

@Serializable
data class AddPersonnel(
    val name: String,
    val email: String,
    val password: String,
    val role: String,
    val donationCenterId: Int,
)