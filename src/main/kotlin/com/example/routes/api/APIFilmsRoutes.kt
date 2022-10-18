package com.example.routes.api

import com.example.models.Comment
import com.example.models.Movie
import com.example.models.commentStorage
import com.example.models.movieStorage
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Route.apiFilmsRouting() {
    route("api") {
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
        /*post("add") {
            val movie = call.receive<Movie>()

            val customer =
                movieStorage.find { it.id == movie.id }

            if(customer != null) {
                return@post call.respondText(
                    "Already exists a movie with that ID",
                    status = HttpStatusCode.BadRequest)
            }

            movieStorage.add(movie)
            call.respondText("Movie stored correctly", status = HttpStatusCode.Created)
        }*/
        post("add") {
            var movieTitle = ""
            var movieYear = ""
            var movieGender = ""
            var movieDirector = ""
            var movieImage = ""

            var fileName = ""

            val datos = call.receiveMultipart()
            datos.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        if(part.name == "title")
                            movieTitle = part.value
                        if(part.name == "year")
                            movieYear = part.value
                        if(part.name == "gender")
                            movieGender = part.value
                        if(part.name == "director")
                            movieDirector = part.value
                        if(part.name == "image")
                            movieImage = part.value
                    }

                    //Segona part del when
                    is PartData.FileItem -> {
                        fileName = part.originalFileName as String
                        var fileBytes = part.streamProvider().readBytes()
                        File("/dades/NGOMEZ/M07/MoviesAPI/uploads/$fileName").writeBytes(fileBytes)
                    }
                    else -> {}
                }}
            val mov = Movie((movieStorage.size+1).toString(), movieTitle, movieYear, movieGender, movieDirector, movieImage)
            movieStorage.add(mov)
            call.respondText("Film stored correctly and \"${mov.image} is uploaded to 'uploads/${mov.image}'\"", status = HttpStatusCode.Created)
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
            val newMovie = call.receive<Movie>()

            val id = call.parameters["id"] ?: return@post call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            movieStorage.find { it.id == id } ?: return@post call.respondText(
                "No movie with id $id",
                status = HttpStatusCode.NotFound
            )

            movieStorage.removeIf { it.id == id }
            movieStorage.add(newMovie)
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
            call.respondText("Movie removed correctly", status = HttpStatusCode.Created)
        }

        get("id/comments/{id?}") {
            //
        }

        get("uploads/{imageName}") {
            val imageName = call.parameters["imageName"]
            var file = File("./uploads/$imageName")
            if(file.exists()){
                call.respondFile(File("./uploads/$imageName"))
            }
            else{
                call.respondText("Image not found", status = HttpStatusCode.NotFound)
            }
        }
    }
}
