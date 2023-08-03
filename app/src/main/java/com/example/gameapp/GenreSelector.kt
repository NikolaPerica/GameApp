package com.example.gameapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gameapp.data.Genre
import com.example.gameapp.data.GenreListResponse
import com.example.gameapp.data.SharedPreferenceManager
import com.example.gameapp.databinding.FragmentGenreSelectorBinding
import com.example.gameapp.retrofit.RawgApiService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GenreSelector : Fragment(R.layout.fragment_genre_selector) {
    private lateinit var binding: FragmentGenreSelectorBinding
    private lateinit var genreAdapter: GenreAdapter
    private val selectedGenres = mutableListOf<Genre>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).supportActionBar?.title = ""

        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGenreSelectorBinding.bind(view)

        // Initialize the adapter with an empty list
        genreAdapter = GenreAdapter { genre -> onGenreClick(genre) }
        binding.recyclerView.adapter = genreAdapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.rawg.io/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(RawgApiService::class.java)

        // Use the API key stored in the string resources
        val apiKey = getString(R.string.rawg_api_key)
        fetchGenres(apiService, apiKey)

        binding.buttonSaveChanges.setOnClickListener {
            saveSelectedGenres()
            findNavController().navigate(R.id.action_genreSelector_to_gamesList2)
        }
    }

    private fun fetchGenres(apiService: RawgApiService, apiKey: String) {
        // Call the API to fetch genres
        val call = apiService.getGenres(apiKey)

        call.enqueue(object : Callback<GenreListResponse> {
            override fun onResponse(call: Call<GenreListResponse>, response: Response<GenreListResponse>) {
                if (response.isSuccessful) {
                    val genreListResponse = response.body()
                    genreListResponse?.let {
                        displayGenres(it.genres) // Display the fetched genres in the RecyclerView
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error fetching genres: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<GenreListResponse>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "Error fetching genres: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun displayGenres(genres: List<Genre>) {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager // Attach the layout manager to the RecyclerView
        genreAdapter.updateGenres(genres) // Update the adapter with the fetched genres
        setSelectedGenres() // Load saved genres and set them as selected in the adapter
    }

    private fun onGenreClick(genre: Genre) {
        // Toggle selection for the clicked genre
        if (selectedGenres.contains(genre)) {
            selectedGenres.remove(genre)
        } else {
            selectedGenres.add(genre)
        }
    }

    private fun saveSelectedGenres() {
        // Get the list of selected genres from the adapter
        val selectedGenresList = genreAdapter.getSelectedGenres()
        val gson = Gson()
        val selectedGenresJson = gson.toJson(selectedGenresList)
        SharedPreferenceManager.saveSelectedGenresJson(requireContext(), selectedGenresJson) // Save the selected genres using SharedPreferences
        Toast.makeText(requireContext(), "Selected genres saved!", Toast.LENGTH_SHORT).show()
    }

    private fun setSelectedGenres() {
        // Load the saved genres from SharedPreferences
        val savedGenresJson = SharedPreferenceManager.getSelectedGenresJson(requireContext())
        val gson = Gson()
        val type = object : TypeToken<List<Genre>>() {}.type
        val savedGenres = gson.fromJson<List<Genre>>(savedGenresJson, type)

        savedGenres?.let {
            // Find genres from the saved list in the adapter's current list and mark them as selected
            for (genre in it) {
                val position = genreAdapter.genres.indexOfFirst { adapterGenre -> adapterGenre.id == genre.id }
                if (position != -1) {
                    val adapterGenre = genreAdapter.genres[position]
                    adapterGenre.isSelected = true
                }
            }
            genreAdapter.notifyDataSetChanged() // Notify the adapter to update the view with the selected genres
        }
    }
}
