package com.example.gameapp.retrofit

import com.example.gameapp.data.Game
import com.example.gameapp.data.GameListResponse
import com.example.gameapp.data.Genre
import com.example.gameapp.data.GenreListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RawgApiService {
    @GET("games")
    fun getGames(@Query("key") apiKey: String, @Query("genres") genresIds: String): Call<GameListResponse>


    @GET("genres")
    fun getGenres(@Query("key") apiKey: String): Call<GenreListResponse>
}


