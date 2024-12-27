package com.novacodestudios.repository

import com.novacodestudios.entity.*
import com.novacodestudios.model.AddDonation
import com.novacodestudios.model.UpdateDonation
import com.novacodestudios.util.suspendTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere


class DonationRepository {

    suspend fun addDonation(donation: AddDonation): Donation = suspendTransaction {
        val donationEntity = DonationEntity.new {
            appointmentEntity =
                AppointmentEntity.findById(donation.appointmentId) ?: throw Exception("AppointmentEntity not found")
            personnelEntity =
                PersonnelEntity.findById(donation.personnelId) ?: throw Exception("PersonnelEntity not found")
            successful = donation.successful
            donationDate = java.time.LocalDate.parse(donation.donationDate)
            remarks = donation.remarks
        }
        donationEntity.toDonation()
    }

    suspend fun updateDonation(updatedDonation: UpdateDonation): Donation? = suspendTransaction {
        DonationEntity.findByIdAndUpdate(updatedDonation.id) { donationEntity ->
            updatedDonation.appointmentId?.let {
                donationEntity.appointmentEntity =
                    AppointmentEntity.findById(it) ?: throw Exception("AppointmentEntity not found")
            }
            updatedDonation.personnelId?.let {
                donationEntity.personnelEntity =
                    PersonnelEntity.findById(it) ?: throw Exception("PersonnelEntity not found")
            }
            updatedDonation.successful?.let {
                donationEntity.successful = it
            }
            updatedDonation.donationDate?.let {
                donationEntity.donationDate = java.time.LocalDate.parse(it)
            }
            updatedDonation.remarks?.let {
                donationEntity.remarks = it
            }
        }?.toDonation()
    }

    suspend fun deleteDonation(id: Int): Boolean = suspendTransaction {
        val rowsDeleted = DonationTable.deleteWhere { DonationTable.id eq id }
        rowsDeleted == 1
    }

    suspend fun getDonationById(id: Int): Donation? = suspendTransaction {
        DonationEntity.findById(id)?.toDonation()
    }

    suspend fun getDonationsByAppointmentId(appointmentId: Int): List<Donation> = suspendTransaction {
        DonationEntity.find { DonationTable.appointmentId eq appointmentId }.map { it.toDonation() }
    }

    suspend fun getDonationsByDonorId(donorId: Int): List<Donation> = suspendTransaction {
        DonationEntity.find { DonationTable.donorId eq donorId }.map { it.toDonation() }
    }

    suspend fun getDonationsByPersonnelId(personnelId: Int): List<Donation> = suspendTransaction {
        DonationEntity.find { DonationTable.personnelId eq personnelId }.map { it.toDonation() }
    }

    suspend fun getDonationsBySuccessStatus(successful: Boolean): List<Donation> = suspendTransaction {
        DonationEntity.find { DonationTable.successful eq successful }.map { it.toDonation() }
    }

    suspend fun getAllDonations(): List<Donation> = suspendTransaction {
        DonationEntity.all().map { it.toDonation() }
    }
}
