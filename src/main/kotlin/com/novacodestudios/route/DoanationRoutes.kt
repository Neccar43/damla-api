package com.novacodestudios.route

import com.novacodestudios.model.AddDonation
import com.novacodestudios.model.UpdateDonation
import com.novacodestudios.repository.DonationRepository
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.donationRoutes(repository: DonationRepository) {
    route("/donation") {
        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid ID")
            val donation = repository.getDonationById(id) ?: return@get call.respond(HttpStatusCode.NotFound)
            call.respond(HttpStatusCode.OK, donation)
        }
        get("/donor/{id}") {
            val donorId = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid ID")
            val donations = repository.getDonationsByDonorId(donorId)
            call.respond(HttpStatusCode.OK, donations)
        }

        post<AddDonation> {
            call.respond(HttpStatusCode.OK, repository.addDonation(it))
        }

        put<UpdateDonation> {
            val donation = repository.updateDonation(it) ?: return@put call.respond(HttpStatusCode.NotFound)
            call.respond(HttpStatusCode.OK, donation)
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid ID")
            val isSuccess = repository.deleteDonation(id)
            call.respond(HttpStatusCode.OK, isSuccess)
        }
    }
}