package com.example.gameapp.data

import com.google.gson.annotations.SerializedName

data class GenreListResponse(
    @SerializedName("results") val genres: List<Genre>
)


data class Genre(
    val id: Int,
    val name: String,
    val slug: String,
    val games_count: Int,
    val image_background: String,
    var isSelected: Boolean = false
)



data class GenreInfo(
    val id: Int,
    val name: String,
    var isSelected: Boolean = false
)
