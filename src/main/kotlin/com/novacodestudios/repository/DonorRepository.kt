package com.novacodestudios.repository

import com.novacodestudios.entity.Donor
import com.novacodestudios.entity.DonorEntity
import com.novacodestudios.entity.DonorTable
import com.novacodestudios.model.donor.AddDonor
import com.novacodestudios.model.donor.UpdateDonor
import com.novacodestudios.util.suspendTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import java.time.LocalDate


class DonorRepository {

    suspend fun addDonor(donor: AddDonor): Donor = suspendTransaction {
        val donorEntity = DonorEntity.new {
            name = donor.name
            email = donor.email
            password = donor.password
            phone = donor.phone
            bloodGroup = donor.bloodGroup
            lastDonationDate = donor.lastDonationDate?.let { LocalDate.parse(it) }
        }
        donorEntity.toDonor()
    }

    suspend fun updateDonor(updatedDonor: UpdateDonor): Donor? = suspendTransaction {
        DonorEntity.findByIdAndUpdate(updatedDonor.id) { donorEntity ->
            updatedDonor.name?.let { donorEntity.name = it }
            updatedDonor.email?.let { donorEntity.email = it }
            updatedDonor.password?.let { donorEntity.password = it }
            updatedDonor.phone?.let { donorEntity.phone = it }
            updatedDonor.bloodGroup?.let { donorEntity.bloodGroup = it }
            updatedDonor.lastDonationDate?.let {
                donorEntity.lastDonationDate = java.time.LocalDate.parse(it)
            }
        }?.toDonor()
    }

    suspend fun deleteDonor(id: Int): Boolean = suspendTransaction {
        val rowsDeleted = DonorTable.deleteWhere { DonorTable.id eq id }
        rowsDeleted == 1
    }

    suspend fun getDonorById(id: Int): Donor? = suspendTransaction {
        DonorEntity.findById(id)?.toDonor()
    }

    suspend fun getDonorsByBloodGroup(bloodGroup: String): List<Donor> = suspendTransaction {
        DonorEntity.find {
            DonorTable.bloodGroup eq bloodGroup
        }.map { it.toDonor() }
    }

    suspend fun getDonorsByDonationDateRange(startDate: String, endDate: String): List<Donor> = suspendTransaction {
        val start = LocalDate.parse(startDate)
        val end = LocalDate.parse(endDate)
        DonorEntity.find {
            DonorTable.lastDonationDate.between(start, end)
        }.map { it.toDonor() }
    }

    suspend fun getDonorByEmail(email: String): Donor? = suspendTransaction {
        DonorEntity.find {
            DonorTable.email eq email
        }.firstOrNull()?.toDonor()
    }

    suspend fun getAllDonors(): List<Donor> = suspendTransaction {
        DonorEntity.all().map { it.toDonor() }
    }

    suspend fun getEligibleDonors(minMonthsSinceLastDonation: Int): List<Donor> = suspendTransaction {
        val cutoffDate = LocalDate.now().minusMonths(minMonthsSinceLastDonation.toLong())
        DonorEntity.find {
            DonorTable.lastDonationDate lessEq cutoffDate
        }.map { it.toDonor() }
    }
}
