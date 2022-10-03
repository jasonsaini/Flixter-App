package com.example.flixter

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import org.json.JSONArray

@Parcelize
data class Movie(
    val movieId: Int,
    val averageVote: Double,
    val posterImageUrl: String,
    val title: String,
    val overview: String,
   ) : Parcelable{
    @IgnoredOnParcel
    val posterImageUrlPath = "https://image.tmdb.org/t/p/w342/$posterImageUrl"
    companion object{
        fun fromJSONArray(movieJSONArray: JSONArray): List<Movie>{
            val movies = mutableListOf<Movie>()
            for(i in 0 until movieJSONArray.length()) {
                val movieJSON = movieJSONArray.getJSONObject(i)
                movies.add(
                    Movie(
                        movieJSON.getInt("id"),
                        movieJSON.getDouble("vote_average"),
                        movieJSON.getString("poster_path"),
                        movieJSON.getString("title"),
                        movieJSON.getString("overview")
                    )
                )
            }
            return movies
        }
    }
   }