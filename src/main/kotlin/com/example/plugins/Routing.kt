package com.example.plugins

import com.example.models.Comment
import com.example.models.Movie
import com.example.models.commentStorage
import com.example.models.movieStorage
import com.example.routes.api.apiFilmsRouting
import com.example.routes.html.htmlFilmsRouting
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.http.ContentDisposition.Companion.File
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import kotlinx.html.*
import java.io.File

fun Application.configureRouting() {

    routing {
        static("/static") {
            resources("files")
        }
        apiFilmsRouting()
        htmlFilmsRouting()
    }
}
