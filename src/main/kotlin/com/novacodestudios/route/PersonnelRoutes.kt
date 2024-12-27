package com.novacodestudios.route

import com.novacodestudios.model.personnel.AddPersonnel
import com.novacodestudios.model.personnel.PersonnelLoginRequest
import com.novacodestudios.model.personnel.UpdatePersonnel
import com.novacodestudios.repository.PersonnelRepository
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.personnelRoutes(repository: PersonnelRepository) {
    route("/personnel") {
        get {
            call.respond(HttpStatusCode.OK, repository.getAllPersonnel())
        }
        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid ID")

            val personnel = repository.getPersonnelById(id) ?: return@get call.respond(HttpStatusCode.NotFound)
            call.respond(HttpStatusCode.OK, personnel)
        }
        get("/donation-center/{id}") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                val personnelList = repository.getPersonnelListByDonationCenterId(id)
                call.respond(HttpStatusCode.OK, personnelList)
            } catch (e: java.lang.Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.localizedMessage)
            }

        }
        post<AddPersonnel>("/signup") {
            try {
                call.respond(HttpStatusCode.Created, repository.addPersonnel(it))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.localizedMessage)
            }
        }
        put<UpdatePersonnel>() {
            try {
                val personnel = repository.updatePersonnel(it) ?: return@put call.respond(HttpStatusCode.NotFound)
                call.respond(HttpStatusCode.OK, personnel)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.localizedMessage)
            }

        }

        post<PersonnelLoginRequest>("/login") {
            try {
                val personnel = repository.getPersonnelByEmail(it.email) ?: return@post call.respond(
                    HttpStatusCode.Unauthorized,
                    "Böyle bir hesap bulunamadı"
                )

                if (personnel.password != it.password) {
                    return@post call.respond(HttpStatusCode.Unauthorized, "Şifre hatalı")
                }
                call.respond(HttpStatusCode.OK, personnel)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.localizedMessage)
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid ID")
            call.respond(HttpStatusCode.OK, repository.deletePersonnel(id))
        }


    }
}