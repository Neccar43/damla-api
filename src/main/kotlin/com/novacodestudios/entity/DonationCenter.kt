package com.novacodestudios.entity

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.time


object DonationCenterTable : IntIdTable("donation_center") {
    val name = varchar("name", 200)
    val address = varchar("address", 500)
    val latitude = double("latitude")
    val longitude = double("longitude")
    val openingTime = time("opening_time")
    val closingTime = time("closing_time")
}

class DonationCenterEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DonationCenterEntity>(DonationCenterTable)

    var name by DonationCenterTable.name
    var address by DonationCenterTable.address
    var latitude by DonationCenterTable.latitude
    var longitude by DonationCenterTable.longitude
    var openingTime by DonationCenterTable.openingTime
    var closingTime by DonationCenterTable.closingTime
    val personnelList by PersonnelEntity referrersOn PersonnelTable.donationCenterId

    fun toDonationCenter() = DonationCenter(
        id = id.value,
        name = name,
        address = address,
        latitude = latitude,
        longitude = longitude,
        openingTime = openingTime.toString(),
        closingTime = closingTime.toString(),
    )
}

@Serializable
data class DonationCenter(
    val id: Int,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val openingTime: String,
    val closingTime: String,
)


