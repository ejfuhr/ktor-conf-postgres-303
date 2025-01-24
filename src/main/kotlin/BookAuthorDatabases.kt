package com.example

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.yaml.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database
import java.sql.Connection

fun Application.configureBookAuthor() {
    val dbConnection: Connection = connectToPostgres(embedded = false)
    val configs = YamlConfig("application.yaml")

    fun getDb() = Database.connect(
       url = configs?.property("postgres.url")?.getString()!!,
        user = configs.property("postgres.user").getString(),
        driver = "org.postgresql.Driver",
        password = configs.property("postgres.password").getString()
    )

    val libraryService = LibraryService(getDb())

    routing {

        post("/authors") {
            val author = call.receive<Author>()
            val id = libraryService.addAuthor(author)
            call.respond(HttpStatusCode.Created, id)
        }

        put("/authors/updateById/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val author = call.receive<Author>()
            libraryService.updateAuthor(id, author.name)
            call.respond(HttpStatusCode.OK, author)

        }

        delete("/authors/deleteById/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val no = libraryService.deleteAuthorById(id)
            call.respond(HttpStatusCode.OK, no)
        }

        get("/authors/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val author = libraryService.readAuthor(id)
            if (author != null) {
                call.respond(HttpStatusCode.Created, author)
            }
        }

        get("/authors") {
            val authors = libraryService.queryAllAuthors()
            call.respond(HttpStatusCode.OK, authors)
        }

        post("/books") {
            val book = call.receive<Book>()
            val id = libraryService.addBook(book)
            call.respond(HttpStatusCode.Created, id)
        }

        put("/books/updateById/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val book = call.receive<Book>()
            libraryService.updateBook(id, book.title)
            call.respond(HttpStatusCode.OK, book)
        }

        delete("/books/deleteById/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val no = libraryService.deleteBookById(id)
            call.respond(HttpStatusCode.OK, no)
        }

        get("/books/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val book = libraryService.readBook(id)
            if (book != null) {
                call.respond(HttpStatusCode.Created, book)
            }
        }

        get("/books") {
            val books = libraryService.queryAllBooks()
            call.respond(HttpStatusCode.OK, books)
        }

        get("/books/authorsByBookId/{id") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val authors = libraryService.getAuthorsByBook(id)
            call.respond(HttpStatusCode.OK, authors)
        }

        get("/books/booksByAuthorId/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val books = libraryService.getBooksByAuthor(id)
            call.respond(HttpStatusCode.OK, books)
        }


    }



    //from orig Databases.kt
/*    val database = Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = "",
    )*/


}

