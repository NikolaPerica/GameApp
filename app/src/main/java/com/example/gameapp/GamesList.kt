package com.example.gameapp

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gameapp.data.Game
import com.example.gameapp.data.GameListResponse
import com.example.gameapp.data.GenreInfo
import com.example.gameapp.data.SharedPreferenceManager
import com.example.gameapp.databinding.FragmentGamesListBinding
import com.example.gameapp.retrofit.RawgApiService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GamesList : Fragment(R.layout.fragment_games_list) {

    // Binding for the fragment's layout
    private lateinit var binding: FragmentGamesListBinding

    // Adapter for displaying the list of games
    private val gamesAdapter = GamesAdapter { game -> onGameClick(game) }

    // List to store the selected genres
    private val selectedGenres = mutableListOf<GenreInfo>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Set the title in the ActionBar
        (activity as MainActivity).supportActionBar?.title = ""

        super.onViewCreated(view, savedInstanceState)
        // Bind the view using the layout binding
        binding = FragmentGamesListBinding.bind(view)

        // Set up the RecyclerView with the adapter and layout manager
        binding.recyclerViewGames.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewGames.adapter = gamesAdapter

        // Set up the click listener for the settings icon to navigate to genre selection
        val settings = view.findViewById<ImageView>(R.id.settings)
        settings.setOnClickListener {
            findNavController().navigate(R.id.action_gamesList_to_genreSelector)
        }

        // Load saved genres from SharedPreferences
        loadSavedGenres()

        // Pass the required values when calling fetchGames function
        val apiKey = getString(R.string.rawg_api_key)
        val genresIds = selectedGenres.joinToString(",") { it.id.toString() }
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.rawg.io/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(RawgApiService::class.java)

        // Call fetchGames and pass apiKey, apiService, and genresIds
        fetchGames(apiService, apiKey, genresIds)
    }

    // Function to fetch games from the API based on selected genres
    private fun fetchGames(apiService: RawgApiService, apiKey: String, genresIds: String) {
        val call = apiService.getGames(apiKey, genresIds)

        call.enqueue(object : Callback<GameListResponse> {
            override fun onResponse(call: Call<GameListResponse>, response: Response<GameListResponse>) {
                if (response.isSuccessful) {
                    val gameListResponse = response.body()
                    gameListResponse?.let {
                        gamesAdapter.updateGames(it.games)
                    }
                } else {
                    // Show error message if API call is not successful
                    Toast.makeText(
                        requireContext(),
                        "Error fetching games: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<GameListResponse>, t: Throwable) {
                // Show error message on network failure
                Toast.makeText(
                    requireContext(),
                    "Error fetching games: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    // Function to load saved genres from SharedPreferences
    private fun loadSavedGenres() {
        val savedGenresJson = SharedPreferenceManager.getSelectedGenresJson(requireContext())
        val gson = Gson()
        val type = object : TypeToken<List<GenreInfo>>() {}.type
        val savedGenres: List<GenreInfo>? = gson.fromJson(savedGenresJson.toString(), type)

        if (savedGenres != null) {
            selectedGenres.addAll(savedGenres)
        }
    }

    // Function to handle game item clicks and navigate to game details fragment
    private fun onGameClick(game: Game) {
        val args = Bundle().apply {
            putInt("gameId", game.id)
        }
        findNavController().navigate(R.id.action_gamesList_to_gameDetails, args)
    }
}
