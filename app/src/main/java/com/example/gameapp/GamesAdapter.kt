package com.example.gameapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gameapp.data.Game
import com.example.gameapp.data.Genre

// Custom RecyclerView adapter for displaying a list of games.
class GamesAdapter(
    private val onGameClick: (Game) -> Unit // Click listener for games
) : RecyclerView.Adapter<GamesAdapter.GameViewHolder>() {

    // List to hold the games data
    private val games: MutableList<Game> = mutableListOf()

    // Function to create the view holder for each game item in the list.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        // Inflate the game item layout and return the view holder
        val view = LayoutInflater.from(parent.context).inflate(R.layout.game_item_layout, parent, false)
        return GameViewHolder(view)
    }

    // Function to bind the data to the view holder when it is bound to a position in the list.
    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]
        holder.bind(game)
    }

    // Function to return the number of items in the list.
    override fun getItemCount(): Int = games.size

    // Inner class representing the view holder for each game item.
    inner class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val gameNameTextView: TextView = itemView.findViewById(R.id.gameNameTextView)

        // Function to bind the data to the view holder.
        fun bind(game: Game) {
            // Set the game name in the TextView
            gameNameTextView.text = game.name

            // Set an onClickListener on the itemView to handle game item clicks.
            itemView.setOnClickListener {
                // Notify the click listener when a game is clicked, passing the clicked game object.
                onGameClick(game)
            }
        }
    }

    // Function to update the list of games with new data.
    fun updateGames(newGames: List<Game>) {
        // Clear the existing games list and add all the new games.
        games.clear()
        games.addAll(newGames)
        // Notify the adapter that the data has changed, so the RecyclerView updates accordingly.
        notifyDataSetChanged()
    }
}
