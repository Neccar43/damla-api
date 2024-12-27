package com.novacodestudios.route

import com.novacodestudios.model.donor.AddDonor
import com.novacodestudios.model.donor.DonorLoginRequest
import com.novacodestudios.repository.DonorRepository
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.donorRoutes(repository: DonorRepository) {
    route("/donor") {
        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid ID")
            println(id)
            val donor = repository.getDonorById(id) ?: return@get call.respond(
                HttpStatusCode.NotFound,
                "Bu id'ye sahip bir donor bulunamadı"
            )
            call.respond(HttpStatusCode.OK, donor)
        }

        post<DonorLoginRequest>("/login") { loginRequest ->
            try {
                val donor = repository.getDonorByEmail(loginRequest.email)
                if (donor == null) {
                    return@post call.respond(HttpStatusCode.Unauthorized, "Böyle bir hesap bulunamadı")
                }

                if (donor.password != loginRequest.password) {
                    return@post call.respond(HttpStatusCode.Unauthorized, "Şifre hatalı")
                }
                call.respond(HttpStatusCode.OK, donor)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.localizedMessage)
            }
        }

        post<AddDonor>("/signup") { addDonor ->
            try {
                println(addDonor)
                val donor = repository.addDonor(addDonor)
                call.respond(HttpStatusCode.Created, donor)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.localizedMessage)
            }
        }

    }
}