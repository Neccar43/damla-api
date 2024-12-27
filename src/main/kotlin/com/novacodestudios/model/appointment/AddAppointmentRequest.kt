package com.novacodestudios.model.appointment

import kotlinx.serialization.Serializable

@Serializable
data class AddAppointmentRequest(
    val appointment: AddAppointment,
    val answers: List<InsertAnswer>
)

@Serializable
data class InsertAnswer(
    val questionId: Int,
    val answerText: String,
)
