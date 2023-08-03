package com.example.gameapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.gameapp.data.Game
import com.example.gameapp.retrofit.GameApiService
import com.google.gson.annotations.SerializedName
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Fragment class for displaying detailed information about a game.
class GameDetails : Fragment(R.layout.fragment_game_details) {

    // Initialize a lateinit variable to store the fetched game details.
    private lateinit var game: Game

    // Function that is called once the fragment's view is created.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Hide the title in the ActionBar (Toolbar) since we are displaying detailed information about a game.
        (activity as MainActivity).supportActionBar?.title = ""

        super.onViewCreated(view, savedInstanceState)

        // Retrieve the game ID passed as an argument from the navigation action.
        val gameId = arguments?.getInt("gameId")
        gameId?.let {
            // Fetch game details using the retrieved game ID.
            fetchGameDetails(gameId)
        }
    }

    // Function to fetch game details from the API using Retrofit.
    private fun fetchGameDetails(gameId: Int) {
        val apiKey = "d99bdf3eb2414415836d5cef9a1344aa"
        val baseUrl = "https://api.rawg.io/api/"
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val gameApiService = retrofit.create(GameApiService::class.java)
        val call = gameApiService.getGame(gameId, apiKey)

        call.enqueue(object : Callback<GameResponse> {
            override fun onResponse(call: Call<GameResponse>, response: Response<GameResponse>) {
                if (response.isSuccessful) {
                    val gameResponse = response.body()
                    if (gameResponse != null) {
                        // Convert the received GameResponse to a Game object and store it.
                        game = gameResponse.toGame()
                        // Display the fetched game details on the screen.
                        displayGameDetails()
                    } else {
                        Toast.makeText(requireContext(), "Game not found!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Error fetching game details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GameResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Function to display the fetched game details on the UI.
    private fun displayGameDetails() {
        // Set the game name, release date, and rating in their respective TextViews.
        view?.findViewById<TextView>(R.id.gameName)?.text = "Game Name: ${game.name}"
        view?.findViewById<TextView>(R.id.releaseDate)?.text = "Released: ${game.released}"
        view?.findViewById<TextView>(R.id.gameRating)?.text = "Rating: ${game.rating}"

        // Load and display the game image using Picasso library.
        val gameImage: ImageView? = view?.findViewById(R.id.gameImage)
        Picasso.get().load(game.background_image).into(gameImage)

        // Display the game genres in the genres TextView.
        val genresTextView: TextView? = view?.findViewById(R.id.gameGenres)
        genresTextView?.text = "Genres: ${game.genres.joinToString(", ") { it.name }}"

        // Additional UI elements can be added here to display more game details.
    }
}

// Data class representing the API response for a game.
data class GameResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("released") val released: String,
    @SerializedName("background_image") val backgroundImage: String,
    @SerializedName("rating") val rating: Double,
    @SerializedName("genres") val genres: List<Genre>
) {
    // Function to convert the GameResponse object to a Game object.
    fun toGame(): Game {
        return Game(id, name, released, backgroundImage, rating, genres)
    }
}

// Data class representing the genre of a game.
data class Genre(
    @SerializedName("name") val name: String
)
