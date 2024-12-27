package com.novacodestudios.repository

import com.novacodestudios.entity.*
import com.novacodestudios.model.appointment.AddAppointment
import com.novacodestudios.model.appointment.UpdateAppointment
import com.novacodestudios.util.suspendTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere

class AppointmentRepository {

    suspend fun addAppointment(appointment: AddAppointment): Appointment = suspendTransaction {
        val appointmentEntity = AppointmentEntity.new {
            appointmentDate = java.time.LocalDateTime.parse(appointment.appointmentDate)
            donor = DonorEntity.findById(appointment.donorId) ?: throw Exception("DonorEntity not found")
            donationCenter =
                DonationCenterEntity.findById(appointment.centerId) ?: throw Exception("DonationCenterEntity not found")
            status = appointment.status
        }
        appointmentEntity.toAppointment()
    }

    suspend fun updateAppointment(updatedAppointment: UpdateAppointment): Appointment? = suspendTransaction {
        AppointmentEntity.findByIdAndUpdate(updatedAppointment.id) { appointmentEntity ->
            updatedAppointment.appointmentDate?.let {
                appointmentEntity.appointmentDate = java.time.LocalDateTime.parse(it)
            }
            updatedAppointment.donorId?.let {
                appointmentEntity.donor = DonorEntity.findById(it) ?: throw Exception("DonorEntity not found")
            }
            updatedAppointment.centerId?.let {
                appointmentEntity.donationCenter =
                    DonationCenterEntity.findById(it) ?: throw Exception("DonationCenterEntity not found")
            }
            updatedAppointment.status?.let {
                appointmentEntity.status = it
            }
        }?.toAppointment()
    }

    suspend fun deleteAppointment(id: Int): Boolean = suspendTransaction {
        val rowsDeleted = AppointmentTable.deleteWhere { AppointmentTable.id eq id }
        rowsDeleted == 1
    }

    suspend fun getAppointmentById(id: Int): Appointment? = suspendTransaction {
        AppointmentEntity.findById(id)?.toAppointment()
    }

    suspend fun getAppointmentsByDonorId(donorId: Int): List<Appointment> = suspendTransaction {
        AppointmentEntity.find { AppointmentTable.donorId eq donorId }.map { it.toAppointment() }
    }

    suspend fun getDonorActiveAppointment(donorId: Int): Appointment? = suspendTransaction {
        AppointmentEntity.find { (AppointmentTable.donorId eq donorId) and (AppointmentTable.status eq AppointmentStatus.SCHEDULED) }
            .singleOrNull()?.toAppointment()
    }

    suspend fun getAppointmentsByCenterId(centerId: Int): List<Appointment> = suspendTransaction {
        AppointmentEntity.find { AppointmentTable.donationCenterId eq centerId }.map { it.toAppointment() }
    }

    suspend fun getAppointmentsByStatus(status: AppointmentStatus): List<Appointment> = suspendTransaction {
        AppointmentEntity.find { AppointmentTable.status eq status }.map { it.toAppointment() }
    }

    suspend fun getAllAppointments(): List<Appointment> = suspendTransaction {
        AppointmentEntity.all().map { it.toAppointment() }
    }
}
