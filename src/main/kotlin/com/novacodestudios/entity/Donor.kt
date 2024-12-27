package com.novacodestudios.entity

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date
import java.time.LocalDate
import java.time.temporal.ChronoUnit

object DonorTable : IntIdTable("donor") {
    val name = varchar("name", 100)
    val email = varchar("email", 100).uniqueIndex()
    val password = varchar("password", 100)
    val phone = varchar("phone", 20)
    val bloodGroup = varchar("blood_group", 10)
    val lastDonationDate = date("last_donation_date").nullable()
}

class DonorEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DonorEntity>(DonorTable)

    var name by DonorTable.name
    var email by DonorTable.email
    var password by DonorTable.password
    var phone by DonorTable.phone
    var bloodGroup by DonorTable.bloodGroup
    var lastDonationDate by DonorTable.lastDonationDate

    fun toDonor() = Donor(
        id = id.value,
        name = name,
        email = email,
        password = password,
        phone = phone,
        bloodGroup = bloodGroup,
        lastDonationDate = lastDonationDate?.toString(),
        isSuitable = lastDonationDate?.let { computeSuitableStatus(it) } != false
    )
}

private fun computeSuitableStatus(lastDonationDate: LocalDate): Boolean {
    val today = LocalDate.now()

    val monthsBetween = ChronoUnit.MONTHS.between(lastDonationDate, today)

    return monthsBetween >= 4
}

@Serializable
data class Donor(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val phone: String,
    val bloodGroup: String,
    val lastDonationDate: String?,
    val isSuitable: Boolean
)
