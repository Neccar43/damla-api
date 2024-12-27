package com.novacodestudios.model

import kotlinx.serialization.Serializable

@Serializable
data class AddQuestion(
    val questionText: String
)

@Serializable
data class UpdateQuestion(
    val id: Int,
    val questionText: String? = null
)
