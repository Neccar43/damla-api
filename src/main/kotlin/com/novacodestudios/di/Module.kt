package com.novacodestudios.di

import com.novacodestudios.entity.*
import com.novacodestudios.repository.*
import com.typesafe.config.ConfigFactory
import io.ktor.server.config.ApplicationConfig
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object Module {
    val answerRepository by lazy { AnswerRepository() }
    val appointmentRepository by lazy { AppointmentRepository() }
    val donationRepository by lazy { DonationRepository() }
    val donorRepository by lazy { DonorRepository() }
    val notificationRepository by lazy { NotificationRepository() }
    val personnelRepository by lazy { PersonnelRepository() }
    val questionRepository by lazy { QuestionRepository() }
    val donationCenterRepository by lazy { DonationCenterRepository() }

    fun initializeDatabase() {

        val database = Database.connect(
            url = "jdbc:postgresql://localhost:5432/damla_db",
            driver = "org.postgresql.Driver",
            user = "root",
            password = "123456"
        )



        transaction(database) {
            SchemaUtils.create(
                AnswerTable, AppointmentTable, DonorTable, DonationCenterTable, DonationTable,
                NotificationTable, PersonnelTable, QuestionTable
            )
        }
    }
}