package com.novacodestudios.route

import com.novacodestudios.model.AddQuestion
import com.novacodestudios.model.UpdateQuestion
import com.novacodestudios.repository.QuestionRepository
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.questionRoutes(repository: QuestionRepository) {
    route("/question") {
        get {
            val questions = repository.getAllQuestions()
            call.respond(HttpStatusCode.OK, questions)
        }

        /*post<AddQuestion>(){
            call.respond(HttpStatusCode.OK,repository.addQuestion(it))
        }*/
        post<List<AddQuestion>> {
            call.respond(HttpStatusCode.OK, repository.addQuestions(it))
        }

        put<UpdateQuestion>() {
            val question = repository.updateQuestion(it) ?: return@put call.respond(HttpStatusCode.NotFound)
            call.respond(HttpStatusCode.OK, question)
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid ID")
            call.respond(HttpStatusCode.OK, repository.deleteQuestion(id))
        }
    }
}