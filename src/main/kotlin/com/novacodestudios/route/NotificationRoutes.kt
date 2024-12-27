package com.novacodestudios.route

import com.novacodestudios.model.AddNotification
import com.novacodestudios.model.UpdateNotification
import com.novacodestudios.repository.NotificationRepository
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.notificationRoutes(repository: NotificationRepository) {
    route("/notification") {
        get("/active") {
            val notifications = repository.getActiveNotifications()
            call.respond(HttpStatusCode.OK, notifications)
        }
        get {
            val notifications = repository.getAllNotifications()
            call.respond(HttpStatusCode.OK, notifications)
        }
        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid ID")
            val notification = repository.getNotificationById(id) ?: return@get call.respond(HttpStatusCode.NotFound)
            call.respond(HttpStatusCode.OK, notification)
        }
        post<AddNotification>() {
            call.respond(HttpStatusCode.OK, repository.addNotification(it))
        }
        put<UpdateNotification>() {
            val notification = repository.updateNotification(it) ?: return@put call.respond(HttpStatusCode.NotFound)
            call.respond(HttpStatusCode.OK, notification)
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid ID")
            call.respond(HttpStatusCode.OK, repository.deleteNotification(id))
        }
    }
}