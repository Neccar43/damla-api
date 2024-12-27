package com.novacodestudios

import com.novacodestudios.di.Module
import com.novacodestudios.plugins.configureHTTP
import com.novacodestudios.plugins.configureRouting
import com.novacodestudios.plugins.configureSecurity
import com.novacodestudios.plugins.configureSerialization
import io.ktor.server.application.*
import net.mamoe.yamlkt.toYamlElement

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    Module.initializeDatabase()
    configureSecurity()
    configureHTTP()
    configureSerialization()
    configureRouting()

    /*install(CallLogging){
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
        format { call ->
            val startTime = System.currentTimeMillis()
            val status = call.response.status()
            val httpMethod = call.request.httpMethod.value
            val userAgent = call.request.headers["User-Agent"]
            val path = call.request.path()
            val queryParams =
                call.request.queryParameters
                    .entries()
                    .joinToString(", ") { "${it.key}=${it.value}" }
            val endTime = System.currentTimeMillis() // Bitiş zaman damgası
            val duration = endTime - startTime // İşlem süresi hesaplama
            val remoteHost = call.request.origin.remoteHost
            val coloredStatus =
                when {
                    status == null -> "\u001B[33mUNKNOWN\u001B[0m"
                    status.value < 300 -> "\u001B[32m$status\u001B[0m"
                    status.value < 400 -> "\u001B[33m$status\u001B[0m"
                    else -> "\u001B[31m$status\u001B[0m"
                }
            val coloredMethod = "\u001B[36m$httpMethod\u001B[0m"
            """
            |
            |------------------------ Request Details ------------------------
            |Status: $coloredStatus
            |Method: $coloredMethod
            |Path: $path
            |Query Params: $queryParams
            |Remote Host: $remoteHost
            |User Agent: $userAgent
            |Duration: ${duration}ms
            |------------------------------------------------------------------
            |
      """.trimMargin()
        }
    }*/
}
