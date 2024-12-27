package com.novacodestudios.model.appointment

import com.novacodestudios.entity.AppointmentStatus
import kotlinx.serialization.Serializable

@Serializable
data class UpdateAppointment(
    val id: Int,
    val appointmentDate: String? = null,
    val donorId: Int? = null,
    val centerId: Int? = null,
    val status: AppointmentStatus? = null,
)