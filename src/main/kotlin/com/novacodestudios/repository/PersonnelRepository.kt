package com.novacodestudios.repository

import com.novacodestudios.entity.DonationCenterEntity
import com.novacodestudios.entity.Personnel
import com.novacodestudios.entity.PersonnelEntity
import com.novacodestudios.entity.PersonnelTable
import com.novacodestudios.exception.EntityNotFoundException
import com.novacodestudios.model.personnel.AddPersonnel
import com.novacodestudios.model.personnel.UpdatePersonnel
import com.novacodestudios.util.suspendTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere


class PersonnelRepository {

    suspend fun addPersonnel(personnel: AddPersonnel): Personnel = suspendTransaction {
        val personnelEntity = PersonnelEntity.new {
            name = personnel.name
            email = personnel.email
            password = personnel.password
            role = personnel.role
            donationCenterEntity = DonationCenterEntity.findById(personnel.donationCenterId)
                ?: throw EntityNotFoundException("donationCenterEntity")
        }
        personnelEntity.toPersonnel()
    }

    suspend fun updatePersonnel(updatedPersonnel: UpdatePersonnel): Personnel? = suspendTransaction {
        PersonnelEntity.findByIdAndUpdate(updatedPersonnel.id) { personnelEntity ->
            updatedPersonnel.name?.let { personnelEntity.name = it }
            updatedPersonnel.email?.let { personnelEntity.email = it }
            updatedPersonnel.password?.let { personnelEntity.password = it }
            updatedPersonnel.role?.let { personnelEntity.role = it }
            updatedPersonnel.donationCenterId?.let {
                personnelEntity.donationCenterEntity =
                    DonationCenterEntity.findById(it) ?: throw EntityNotFoundException("donationCenterEntity")
            }
        }?.toPersonnel()
    }

    suspend fun deletePersonnel(id: Int): Boolean = suspendTransaction {
        val rowsDeleted = PersonnelTable.deleteWhere { PersonnelTable.id eq id }
        rowsDeleted == 1
    }

    suspend fun getPersonnelById(id: Int): Personnel? = suspendTransaction {
        PersonnelEntity.findById(id)?.toPersonnel()
    }

    suspend fun getPersonnelByRole(role: String): List<Personnel> = suspendTransaction {
        PersonnelEntity.find {
            PersonnelTable.role eq role
        }.map(PersonnelEntity::toPersonnel)
    }

    suspend fun getPersonnelByEmail(email: String): Personnel? = suspendTransaction {
        PersonnelEntity.find {
            PersonnelTable.email eq email
        }.firstOrNull()?.toPersonnel()
    }

    suspend fun getAllPersonnel(): List<Personnel> = suspendTransaction {
        PersonnelEntity.all().map(PersonnelEntity::toPersonnel)
    }

    suspend fun searchPersonnelByName(keyword: String): List<Personnel> = suspendTransaction {
        PersonnelEntity.find {
            PersonnelTable.name like "%$keyword%"
        }.map(PersonnelEntity::toPersonnel)
    }

    suspend fun getPersonnelListByDonationCenterId(donationCenterId: Int): List<Personnel> = suspendTransaction {
        DonationCenterEntity.findById(donationCenterId)?.personnelList?.map(PersonnelEntity::toPersonnel)
            ?: throw EntityNotFoundException()
    }
}
