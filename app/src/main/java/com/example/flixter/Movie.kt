package com.example.flixter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Movie(
    val movieId: Int,
    val averageVote: Double,
    val posterImageUrl: String,
    val title: String,
    val overview: String,
   ) : Parcelable{


}