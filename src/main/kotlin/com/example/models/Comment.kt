package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Comment(val id: String, val movie_id: String, var comment: String, val creation_date: String) {}

val commentStorage = mutableListOf<Comment>()
