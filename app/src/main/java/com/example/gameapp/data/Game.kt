package com.example.gameapp.data

import com.example.gameapp.Genre
import com.google.gson.annotations.SerializedName

data class Game(
    val id: Int,
    val name: String,
    val released: String,
    val background_image: String,
    val rating: Double,
    val genres: List<Genre>
)

data class GameListResponse(
    @SerializedName("results") val games: List<Game>
)
