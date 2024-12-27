package com.novacodestudios.model.personnel

import kotlinx.serialization.Serializable

@Serializable
data class PersonnelLoginRequest(
    val email: String,
    val password: String
)
