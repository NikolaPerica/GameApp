package com.example.gameapp.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SharedPreferenceManager {

    private const val SHARED_PREFS_NAME = "MyPrefs"
    private const val KEY_SELECTED_GENRES = "selectedGenres"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveSelectedGenresJson(context: Context, selectedGenresJson: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(KEY_SELECTED_GENRES, selectedGenresJson)
        editor.apply()
    }

    fun getSelectedGenresJson(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_SELECTED_GENRES, null)
    }
}

