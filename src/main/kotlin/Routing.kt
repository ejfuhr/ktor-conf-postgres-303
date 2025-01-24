package com.example

import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.webjars.*
import java.sql.Connection
import java.sql.DriverManager
import kotlinx.css.*
import kotlinx.html.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.*

fun Application.configureRouting() {

    install(ContentNegotiation) {
        json()
        register(ContentType.Text.Html, KotlinxSerializationConverter(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true

        }))
    }

    install(Webjars) {
        path = "/assets" //defaults to /webjars
    }
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        // Static plugin. Try to access `/static/index.html`
        staticResources("/static", "static")
/*        get("/webjars") {
            call.respondText("<script src='/webjars/jquery/jquery.js'></script>", ContentType.Text.Html)
        }*/
    }
}
