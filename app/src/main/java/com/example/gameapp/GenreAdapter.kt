package com.example.gameapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gameapp.data.Genre

class GenreAdapter(
    private val onGenreClick: (Genre) -> Unit // Click listener for genres
) : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    // List of genres to be displayed in the RecyclerView
    var genres: MutableList<Genre> = mutableListOf()
        private set // Make the list accessible but not modifiable from outside the adapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        // Inflate the layout for each genre item view
        val view = LayoutInflater.from(parent.context).inflate(R.layout.genre_item_layout, parent, false)
        return GenreViewHolder(view)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        // Bind each genre item to its view holder
        val genre = genres[position]
        holder.bind(genre)
    }

    override fun getItemCount(): Int = genres.size

    inner class GenreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Views to be displayed for each genre item
        private val genreNameTextView: TextView = itemView.findViewById(R.id.genreTextView)
        private val checkBoxGenre: CheckBox = itemView.findViewById(R.id.checkBoxGenre)

        fun bind(genre: Genre) {
            // Bind the genre data to the views
            genreNameTextView.text = genre.name
            checkBoxGenre.isChecked = genre.isSelected // Set the initial checked state of the checkbox

            // Set a click listener on the checkbox to handle genre selection changes
            checkBoxGenre.setOnCheckedChangeListener { _, isChecked ->
                genre.isSelected = isChecked // Update the isSelected property of the genre
                onGenreClick(genre) // Notify the click listener about the genre selection change
            }
        }
    }

    // Function to update the list of genres
    fun updateGenres(newGenres: List<Genre>) {
        genres.clear()
        genres.addAll(newGenres)
        notifyDataSetChanged()
    }

    // Function to get the selected genres
    fun getSelectedGenres(): List<Genre> {
        // Filter and return the genres that are selected
        return genres.filter { it.isSelected }
    }
}
