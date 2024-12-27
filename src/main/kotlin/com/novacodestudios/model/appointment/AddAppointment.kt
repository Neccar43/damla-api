package com.novacodestudios.model.appointment

import com.novacodestudios.entity.AppointmentStatus
import kotlinx.serialization.Serializable

@Serializable
data class AddAppointment(
    val appointmentDate: String,
    val donorId: Int,
    val centerId: Int,
    val status: AppointmentStatus = AppointmentStatus.SCHEDULED,
)