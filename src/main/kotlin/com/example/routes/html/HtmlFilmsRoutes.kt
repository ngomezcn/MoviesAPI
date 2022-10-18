package com.example.routes.html

import com.example.models.movieStorage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import kotlinx.html.*


fun Route.htmlFilmsRouting() {

    route("/") {
        get("list") {

            call.respondHtml(HttpStatusCode.OK) {

                head {
                    title("Llista")
                }

                body {

                    for (i in movieStorage) {
                        h4 { +i.title }
                        img { src = "/uploads/${i.image}" }
                        println(Application::class)
                    }
                }
            }
        }
        get("addFilm") {

            call.respondHtml(HttpStatusCode.OK) {
                body {
                    form {
                        action = "api/add"
                        method = FormMethod.post
                        encType = FormEncType.multipartFormData

                        label { text("Title") }
                        input {
                            type = InputType.text
                            id = "title"
                        }
                        br { }
                        label { text("Year") }
                        input {
                            type = InputType.text
                            id = "year"
                        }
                        br { }
                        label { text("Gender") }
                        input {
                            type = InputType.text
                            id = "gender"
                        }
                        br { }
                        label { text("Director") }
                        input {
                            type = InputType.text
                            id = "director"
                        }
                        br { }
                        label { text("Image") }
                        input {
                            type = InputType.file
                            id = "image"
                        }
                        br { }
                        input {
                            type = InputType.submit
                        }
                    }
                }
            }
        }
    }
}