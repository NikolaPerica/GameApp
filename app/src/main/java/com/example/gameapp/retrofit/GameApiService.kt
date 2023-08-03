package com.example.gameapp.retrofit

import com.example.gameapp.GameResponse
import com.example.gameapp.data.GenreListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GameApiService {
    @GET("games/{gameId}")
    fun getGame(@Path("gameId") gameId: Int, @Query("key") apiKey: String): Call<GameResponse>
}

