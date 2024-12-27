package com.novacodestudios.route

import com.novacodestudios.model.AddAnswer
import com.novacodestudios.model.appointment.AddAppointmentRequest
import com.novacodestudios.model.appointment.UpdateAppointment
import com.novacodestudios.repository.AnswerRepository
import com.novacodestudios.repository.AppointmentRepository
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.appointmentRoutes(
    appointmentRepository: AppointmentRepository,
    answerRepository: AnswerRepository,
) {
    route("/appointment") {
        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid ID")

            val appointment =
                appointmentRepository.getAppointmentById(id) ?: return@get call.respond(HttpStatusCode.NotFound)
            call.respond(HttpStatusCode.OK, appointment)
        }
        get("/active/{donorId}") {
            val donorId = call.parameters["donorId"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid ID")

            val appointment = appointmentRepository.getDonorActiveAppointment(donorId) ?: return@get call.respond(
                HttpStatusCode.NotFound,
                "Aktif randevu bulunamadı"
            )
            call.respond(HttpStatusCode.OK, appointment)

        }

        get("/donor/{id}") {
            val donorId = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid ID")

            val appointment = appointmentRepository.getAppointmentsByDonorId(donorId)
            call.respond(HttpStatusCode.OK, appointment)

        }

        get {
            val appointments = appointmentRepository.getAllAppointments()
            call.respond(HttpStatusCode.OK, appointments)
        }

        put<UpdateAppointment> { updateAppointment ->
            try {
                val appointment = appointmentRepository.updateAppointment(updateAppointment) ?: return@put call.respond(
                    HttpStatusCode.NotFound
                )
                call.respond(HttpStatusCode.OK, appointment)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.localizedMessage)
            }
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid ID")
            val isSuccess = appointmentRepository.deleteAppointment(id)
            call.respond(HttpStatusCode.OK, isSuccess)

        }
        post<AddAppointmentRequest> {
            try {
                val appointment = appointmentRepository.addAppointment(it.appointment)
                val answers = answerRepository.addAnswers(it.answers.map { answer ->
                    AddAnswer(
                        questionId = answer.questionId,
                        appointmentId = appointment.id,
                        answerText = answer.answerText
                    )
                })
                call.respond(HttpStatusCode.OK, appointment)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.localizedMessage)
            }

        }
    }
}