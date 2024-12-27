package com.novacodestudios.entity

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object NotificationTable : IntIdTable("notification") {
    val title = text("title") // varchar
    val message = text("message")
    val sentAt = datetime("sent_at")
    val isActive = bool("is_active")
    val requiredBloodType = varchar("required_blood_type", 10)
    val donationCenter = reference("donation_center_id", DonationCenterTable)
}

class NotificationEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NotificationEntity>(NotificationTable)

    var title by NotificationTable.title
    var message by NotificationTable.message
    var sentAt by NotificationTable.sentAt
    var isActive by NotificationTable.isActive
    var requiredBloodType by NotificationTable.requiredBloodType
    var donationCenter by DonationCenterEntity referencedOn NotificationTable.donationCenter

    fun toNotification() = Notification(
        id = id.value,
        title = title,
        message = message,
        sentAt = sentAt.toString(),
        isActive = isActive,
        requiredBloodType = requiredBloodType,
        donationCenter = donationCenter.toDonationCenter()
    )
}

@Serializable
data class Notification(
    val id: Int,
    val title: String,
    val message: String,
    val sentAt: String,
    val isActive: Boolean,
    val requiredBloodType: String,
    val donationCenter: DonationCenter
)
