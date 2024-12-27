package com.novacodestudios.model.personnel

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePersonnel(
    val id: Int,
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val role: String? = null,
    val donationCenterId: Int? = null,
)