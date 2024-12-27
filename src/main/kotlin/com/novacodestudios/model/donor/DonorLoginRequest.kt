package com.novacodestudios.model.donor

import kotlinx.serialization.Serializable

@Serializable
data class DonorLoginRequest(
    val email: String,
    val password: String
)