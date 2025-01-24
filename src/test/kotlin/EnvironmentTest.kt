package com.example

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.config.yaml.*
import io.ktor.server.testing.*
import kotlin.test.*


class EnvironmentTest {

    @Test
    fun `test environment`() = testApplication {
        println("Environment test")

        environment {
            config = MapApplicationConfig(
                "postgres.url" to "jdbc:postgresql://localhost:5432/jpastuff",
                "postgres.user" to "postgres",
                "postgres.password" to "YOUR_PASSWORD"
            )
        }

        application {

            module()
            checkEnvironment()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

    @Test
    fun `test enviro direct`() = testApplication {
        println("Environment test")

        environment {
            val config1 = YamlConfig("application.yaml")
            assertNotNull(config1)
            config1
                .toMap()
                .forEach { t, u ->
                    println("key:: $t -> value:: $u")
                }
        }
        application {
            module()
            checkEnvironment()
        }
        client.get("/enviro").let {
            assertEquals("got full address", it.bodyAsText())
            assertNotNull(it.bodyAsText())
        }
    }
}


fun Application.checkEnvironment() {
    println("Environment check")

}