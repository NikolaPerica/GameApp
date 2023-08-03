package com.example.gameapp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gameapp.data.Genre
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Set the ActionBar title to an empty string
        (activity as MainActivity).supportActionBar?.title = ""

        super.onViewCreated(view, savedInstanceState)

        // Find the button with ID "buttonCheckData" in the fragment_home layout
        val button = view.findViewById<Button>(R.id.buttonCheckData)

        // Set a click listener for the button
        button.setOnClickListener {
            // Check if data is stored in SharedPreferences
            val sharedPreferences = requireContext().getSharedPreferences(
                "MyPrefs", // Replace with your SharedPreferences name
                Context.MODE_PRIVATE
            )

            // Replace "selectedGenres" with the actual key used to store the data in SharedPreferences
            val selectedGenresJson = sharedPreferences.getString("selectedGenres", null)

            if (selectedGenresJson != null && selectedGenresJson.isNotEmpty()) {
                // If data is stored and not empty, parse the JSON data into a list of Genre objects
                val gson = Gson()
                val type = object : TypeToken<List<Genre>>() {}.type
                val selectedGenres: List<Genre> = gson.fromJson(selectedGenresJson, type)

                if (selectedGenres.isNotEmpty()) {
                    // If there are selected genres, navigate to GamesListFragment
                    findNavController().navigate(R.id.action_homeFragment_to_gamesList)
                    return@setOnClickListener // Exit the function early to avoid navigating twice
                }
            }

            // If selectedGenres list is empty or parsing fails, navigate to GenreSelectorFragment
            findNavController().navigate(R.id.action_homeFragment_to_genreSelector)
        }
    }
}
