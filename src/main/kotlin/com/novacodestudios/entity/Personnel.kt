package com.novacodestudios.entity

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object PersonnelTable : IntIdTable("personnel") {
    val name = varchar("name", 100)
    val email = varchar("email", 100).uniqueIndex()
    val password = varchar("password", 100)
    val role = varchar("role", 50)
    val donationCenterId = reference("donation_center_id", DonationCenterTable)
}

class PersonnelEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PersonnelEntity>(PersonnelTable)

    var name by PersonnelTable.name
    var email by PersonnelTable.email
    var password by PersonnelTable.password
    var role by PersonnelTable.role
    var donationCenterEntity by DonationCenterEntity referencedOn PersonnelTable.donationCenterId


    fun toPersonnel() = Personnel(
        id = id.value,
        name = name,
        email = email,
        password = password,
        role = role,
        donationCenter = donationCenterEntity.toDonationCenter()
    )
}

@Serializable
data class Personnel(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val role: String,
    val donationCenter: DonationCenter,
)



