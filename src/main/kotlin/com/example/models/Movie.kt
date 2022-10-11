package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Movie(val id: String, val title: String, val year: String, val gender: String, val director: String, val image: String = "") {}

val movieStorage = mutableListOf<Movie>()
