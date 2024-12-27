package com.novacodestudios.repository

import com.novacodestudios.entity.DonationCenterEntity
import com.novacodestudios.entity.Notification
import com.novacodestudios.entity.NotificationEntity
import com.novacodestudios.entity.NotificationTable
import com.novacodestudios.model.AddNotification
import com.novacodestudios.model.UpdateNotification
import com.novacodestudios.util.suspendTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

class NotificationRepository {

    suspend fun addNotification(notification: AddNotification): Notification = suspendTransaction {
        val donationCenterEntity = DonationCenterEntity.findById(notification.donationCenterId)
            ?: throw IllegalArgumentException("Donation center with ID ${notification.donationCenterId} not found.")

        val notificationEntity = NotificationEntity.new {
            title = notification.title
            message = notification.message
            sentAt = java.time.LocalDateTime.parse(notification.sentAt)
            isActive = notification.isActive
            requiredBloodType = notification.requiredBloodType
            donationCenter = donationCenterEntity
        }
        notificationEntity.toNotification()
    }

    suspend fun updateNotification(updatedNotification: UpdateNotification): Notification? = suspendTransaction {
        NotificationEntity.findById(updatedNotification.id)?.apply {
            updatedNotification.title?.let { title = it }
            updatedNotification.message?.let { message = it }
            updatedNotification.sentAt?.let {
                sentAt = java.time.LocalDateTime.parse(it)
            }
            updatedNotification.isActive?.let { isActive = it }
            updatedNotification.requiredBloodType?.let { requiredBloodType = it }

            updatedNotification.donationCenterId?.let {
                val newDonationCenter = DonationCenterEntity.findById(it)
                    ?: throw IllegalArgumentException("Donation center with ID $it not found.")
                donationCenter = newDonationCenter
            }
        }?.toNotification()
    }

    suspend fun deleteNotification(id: Int): Boolean = suspendTransaction {
        val rowsDeleted = NotificationTable.deleteWhere { NotificationTable.id eq id }
        rowsDeleted == 1
    }

    suspend fun getNotificationById(id: Int): Notification? = suspendTransaction {
        NotificationEntity.findById(id)?.toNotification()
    }

    suspend fun getAllNotifications(): List<Notification> = suspendTransaction {
        NotificationEntity.all().map { it.toNotification() }
    }

    suspend fun getActiveNotifications(): List<Notification> = suspendTransaction {
        NotificationEntity.find { NotificationTable.isActive eq true }
            .map(NotificationEntity::toNotification)
    }

    suspend fun getNotificationsByDateRange(startDate: String, endDate: String): List<Notification> =
        suspendTransaction {
            val start = java.time.LocalDateTime.parse(startDate)
            val end = java.time.LocalDateTime.parse(endDate)
            NotificationEntity.find {
                NotificationTable.sentAt.between(start, end)
            }.map { it.toNotification() }
        }

    suspend fun searchNotificationsByMessage(keyword: String): List<Notification> = suspendTransaction {
        NotificationEntity.find {
            NotificationTable.message like "%$keyword%"
        }.map { it.toNotification() }
    }
}
