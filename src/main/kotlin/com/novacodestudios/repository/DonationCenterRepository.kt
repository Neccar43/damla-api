package com.novacodestudios.repository

import com.novacodestudios.entity.DonationCenter
import com.novacodestudios.entity.DonationCenterEntity
import com.novacodestudios.entity.DonationCenterTable
import com.novacodestudios.model.AddDonationCenter
import com.novacodestudios.model.UpdateDonationCenter
import com.novacodestudios.util.suspendTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


class DonationCenterRepository {

    suspend fun addDonationCenter(center: AddDonationCenter): DonationCenter = suspendTransaction {
        val donationCenterEntity = DonationCenterEntity.new {
            name = center.name
            address = center.address
            latitude = center.latitude
            longitude = center.longitude
            openingTime = java.time.LocalTime.parse(center.openingTime)
            closingTime = java.time.LocalTime.parse(center.closingTime)
        }
        donationCenterEntity.toDonationCenter()
    }

    suspend fun updateDonationCenter(updatedCenter: UpdateDonationCenter): DonationCenter? = suspendTransaction {
        DonationCenterEntity.findByIdAndUpdate(updatedCenter.id) { centerEntity ->
            updatedCenter.name?.let { centerEntity.name = it }
            updatedCenter.address?.let { centerEntity.address = it }
            updatedCenter.latitude?.let { centerEntity.latitude = it }
            updatedCenter.longitude?.let { centerEntity.longitude = it }
            updatedCenter.openingTime?.let {
                centerEntity.openingTime = java.time.LocalTime.parse(it)
            }
            updatedCenter.closingTime?.let {
                centerEntity.closingTime = java.time.LocalTime.parse(it)
            }
        }?.toDonationCenter()
    }

    suspend fun deleteDonationCenter(id: Int): Boolean = suspendTransaction {
        val rowsDeleted = DonationCenterTable.deleteWhere { DonationCenterTable.id eq id }
        rowsDeleted == 1
    }

    suspend fun getDonationCenterById(id: Int): DonationCenter? = suspendTransaction {
        DonationCenterEntity.findById(id)?.toDonationCenter()
    }

    suspend fun getDonationCentersByAddress(addressQuery: String): List<DonationCenter> = suspendTransaction {
        DonationCenterEntity.find {
            DonationCenterTable.address like "%$addressQuery%"
        }.map { it.toDonationCenter() }
    }

    suspend fun getAllDonationCenters(): List<DonationCenter> = suspendTransaction {
        DonationCenterEntity.all().map { it.toDonationCenter() }
    }

    suspend fun findClosestDonationCenter(latitude: Double, longitude: Double): DonationCenter? = suspendTransaction {
        DonationCenterEntity.all()
            .minByOrNull { center ->
                haversineDistance(
                    latitude,
                    longitude,
                    center.latitude,
                    center.longitude
                )
            }?.toDonationCenter()
    }

    private fun haversineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371.0 // Kilometre
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c
    }
}
