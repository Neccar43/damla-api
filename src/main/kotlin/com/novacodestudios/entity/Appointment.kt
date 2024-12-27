package com.novacodestudios.entity

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime


object AppointmentTable : IntIdTable("appointment") {
    val appointmentDate = datetime("appointment_date")
    val donorId = reference("donor_id", DonorTable)
    val donationCenterId = reference("donation_center_id", DonationCenterTable)
    val status = enumeration<AppointmentStatus>("status")
}

class AppointmentEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AppointmentEntity>(AppointmentTable)

    var appointmentDate by AppointmentTable.appointmentDate
    var donor by DonorEntity referencedOn AppointmentTable.donorId
    var donationCenter by DonationCenterEntity referencedOn AppointmentTable.donationCenterId
    var status by AppointmentTable.status
    //val answers by AnswerEntity referrersOn AnswerTable.appointmentId

    fun toAppointment() = Appointment(
        id = id.value,
        date = appointmentDate.toString(),
        donor = donor.toDonor(),
        donationCenter = donationCenter.toDonationCenter(),
        status = status,
    )
}

@Serializable
data class Appointment(
    val id: Int,
    val date: String,
    val donor: Donor,
    val donationCenter: DonationCenter,
    val status: AppointmentStatus,
)

enum class AppointmentStatus {
    SCHEDULED, COMPLETED, CANCELED
}


