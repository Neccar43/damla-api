package com.novacodestudios.entity

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date

object DonationTable : IntIdTable("donation") {
    val appointmentId = reference("appointment_id", AppointmentTable)
    val personnelId = reference("personnel_id", PersonnelTable)
    val donorId = reference("donor_id", DonorTable)
    val successful = bool("successful")
    val donationDate = date("donation_date")
    val remarks = text("remarks")
    val donationCenterId = reference("donation_center_id", DonationCenterTable)
}

class DonationEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DonationEntity>(DonationTable)

    var appointmentEntity by AppointmentEntity referencedOn DonationTable.appointmentId
    var personnelEntity by PersonnelEntity referencedOn DonationTable.personnelId
    var donorEntity by DonorEntity referencedOn DonationTable.donorId
    var successful by DonationTable.successful
    var donationDate by DonationTable.donationDate
    var remarks by DonationTable.remarks
    var donationCenterEntity by DonationCenterEntity referencedOn DonationTable.donationCenterId

    fun toDonation() = Donation(
        id = id.value,
        appointment = appointmentEntity.toAppointment(),
        personnel = personnelEntity.toPersonnel(),
        donor = donorEntity.toDonor(),
        successful = successful,
        donationDate = donationDate.toString(),
        remarks = remarks,
        donationCenter = donationCenterEntity.toDonationCenter(),
    )
}

@Serializable
data class Donation(
    val id: Int,
    val appointment: Appointment,
    val personnel: Personnel,
    val donor: Donor,
    val successful: Boolean,
    val donationDate: String,
    val remarks: String,
    val donationCenter: DonationCenter,
)
