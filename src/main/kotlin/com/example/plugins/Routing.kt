package com.example.plugins

import com.example.models.Comment
import com.example.models.Movie
import com.example.models.commentStorage
import com.example.models.movieStorage
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting() {

    routing {
        get("all") {
            if (movieStorage.isNotEmpty()) {
                call.respond(movieStorage)
            }
        }
        get("id/{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val customer =
                movieStorage.find { it.id == id } ?: return@get call.respondText(
                    "No movie with id $id",
                    status = HttpStatusCode.NotFound
                )
            call.respond(customer)
        }
        post("add") {
            val movie = call.receive<Movie>()
            movieStorage.add(movie)
            call.respondText("Movie stored correctly", status = HttpStatusCode.Created)
        }

        post("id/add/{id?}") {
            val id = call.parameters["id"] ?: return@post call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest)

            movieStorage.find { it.id == id } ?: return@post call.respondText(
                 "No movie with id $id",
                status = HttpStatusCode.NotFound)

            val comment = call.receive<Comment>()
            commentStorage.add(comment)
            call.respondText("Comment stored correctly", status = HttpStatusCode.Created)
        }

        post("update/id/{id?}") {
            val id = call.parameters["id"] ?: return@post call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val movie =
                movieStorage.find { it.id == id } ?: return@post call.respondText(
                    "No movie with id $id",
                    status = HttpStatusCode.NotFound
                )

            movieStorage.removeIf { it.id == id }
            movieStorage.add(movie)
            call.respondText("Movie updated correctly", status = HttpStatusCode.Created)
        }

        delete("delete/id/{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            movieStorage.find { it.id == id } ?: return@delete call.respondText(
                "No movie with id $id",
                status = HttpStatusCode.NotFound
            )

            movieStorage.removeIf { it.id == id }
            call.respondText("Movie deleted correctly", status = HttpStatusCode.Created)
        }

        post("id/comments/{id?}") {
            val id = call.parameters["id"] ?: return@post call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            movieStorage.find { it.id == id } ?: return@post call.respondText(
                "No movie with id $id",
                status = HttpStatusCode.NotFound
            )

            val response = commentStorage.filter { it.movie_id == id }
            call.respond(response)
        }
    }
}
