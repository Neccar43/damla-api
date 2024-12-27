package com.novacodestudios.model

import kotlinx.serialization.Serializable

@Serializable
data class AddAnswer(
    val questionId: Int,
    val appointmentId: Int,
    val answerText: String,
)

@Serializable
data class UpdateAnswer(
    val id: Int,
    val questionId: Int? = null,
    val appointmentId: Int? = null,
    val answerText: String? = null,
)