package com.novacodestudios.plugins

import com.novacodestudios.di.Module
import com.novacodestudios.route.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        answerRoutes(Module.answerRepository)
        appointmentRoutes(
            Module.appointmentRepository,
            answerRepository = Module.answerRepository
        )
        donationRoutes(Module.donationRepository)
        donationCenterRoutes(Module.donationCenterRepository)
        donorRoutes(Module.donorRepository)
        notificationRoutes(Module.notificationRepository)
        personnelRoutes(Module.personnelRepository)
        questionRoutes(Module.questionRepository)

    }
}
