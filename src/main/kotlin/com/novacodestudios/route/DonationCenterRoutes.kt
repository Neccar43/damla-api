package com.novacodestudios.route

import com.novacodestudios.model.AddDonationCenter
import com.novacodestudios.model.UpdateDonationCenter
import com.novacodestudios.repository.DonationCenterRepository
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.donationCenterRoutes(repository: DonationCenterRepository) {
    route("/donation-center") {
        get {
            val centers = repository.getAllDonationCenters()
            call.respond(HttpStatusCode.OK, centers)
        }
        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid ID")

            val center = repository.getDonationCenterById(id) ?: call.respond(HttpStatusCode.NotFound)
            call.respond(HttpStatusCode.OK, center)
        }

        put<UpdateDonationCenter> {
            val donationCenter = repository.updateDonationCenter(it) ?: call.respond(HttpStatusCode.NotFound)
            call.respond(HttpStatusCode.OK, donationCenter)
        }

        post<AddDonationCenter>() {
            println("AddDonationCenter: $it")
            call.respond(HttpStatusCode.OK, repository.addDonationCenter(it))
        }
        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid ID")

            call.respond(HttpStatusCode.OK, repository.deleteDonationCenter(id))
        }


    }
}