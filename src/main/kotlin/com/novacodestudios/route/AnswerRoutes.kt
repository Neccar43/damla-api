package com.novacodestudios.route

import com.novacodestudios.model.AddAnswer
import com.novacodestudios.model.UpdateAnswer
import com.novacodestudios.repository.AnswerRepository
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.answerRoutes(repository: AnswerRepository) {
    route("/answer") {
        put<UpdateAnswer> {
            val answer = repository.updateAnswer(it) ?: return@put call.respond(HttpStatusCode.NotFound)
            call.respond(HttpStatusCode.OK, answer)
        }

        post<List<AddAnswer>> {
            call.respond(HttpStatusCode.OK, repository.addAnswers(it))
        }

        get("/appointment/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid ID")
            call.respond(HttpStatusCode.OK, repository.getAnswersByAppointmentId(id))
        }
    }
}