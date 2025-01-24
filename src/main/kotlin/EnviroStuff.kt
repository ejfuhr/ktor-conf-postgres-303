package com.example

import io.ktor.server.application.*
import io.ktor.server.config.yaml.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.text.get

fun Application.doEnviroStuff() {
    val env = environment.config.propertyOrNull("postgres.url")?.getString()
    val configs = YamlConfig("application.yaml")
    val url = configs?.property("postgres.url")?.getString()
    routing {
        get("/enviro"){
            call.respondText(
                when (url) {
                   //println("url:: $url"),
                    "jdbc:postgresql://localhost:5432/jpastuff" -> "got full address"
                    "xyz" -> "got xyz"
                    else -> "... got nothing or $url"
                }
            )
        }
    }
}