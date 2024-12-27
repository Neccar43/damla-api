package com.novacodestudios.model

import kotlinx.serialization.Serializable

@Serializable
data class AddNotification(
    val title: String,
    val message: String,
    val sentAt: String,
    val isActive: Boolean,
    val requiredBloodType: String,
    val donationCenterId: Int
)

@Serializable
data class UpdateNotification(
    val id: Int,
    val title: String? = null,
    val message: String? = null,
    val sentAt: String? = null,
    val isActive: Boolean? = null,
    val requiredBloodType: String? = null,
    val donationCenterId: Int?
)
