package com.example.flixter

import android.os.Bundle
import android.util.Log
import android.widget.RatingBar
import android.widget.TextView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import okhttp3.Headers

private const val YOUTUBE_EMBEDDED_VIDEO_API_KEY = "AIzaSyA_XgQRzSyHz_N6Has8CStgLRyLPKuZ6_A"
private const val MOVIE_TRAILERS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"


class DetailActivity () : YouTubeBaseActivity() {

    // lateinit vars for UI components
    private lateinit var movieTitle: TextView
    private lateinit var movieOverview: TextView
    private lateinit var ratingMeter: RatingBar
    private lateinit var YTPlayerView: YouTubePlayerView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        // Gather views
        movieTitle = findViewById(R.id.tvTitle)
        movieOverview = findViewById(R.id.tvOverview)
        ratingMeter = findViewById(R.id.rbVoteAverage)
        YTPlayerView = findViewById(R.id.player)

        val movie = intent.getParcelableExtra<Movie>("MOVIE_EXTRA") as Movie

        movieTitle.text = movie.title
        movieOverview.text = movie.overview
        ratingMeter.rating = movie.averageVote.toFloat()

        val client = AsyncHttpClient()
        client.get(MOVIE_TRAILERS_URL.format(movie.movieId), object: JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.i("DetailActivity", "onFailure $statusCode")}

            override fun onSuccess(statusCode: Int, headers:Headers?, json:JSON){
                val results = json.jsonObject.getJSONArray("results")
                if(results.length() == 0)
                {
                    Log.w("DetailActivity", "No movie trailers found")
                }
                val movieTrailerJson = results.getJSONObject(0)
                val ytKey = movieTrailerJson.getString("key")

                initializeYoutube(ytKey)
            }

            })
    }
    private fun initializeYoutube(youtubeKey : String)
    {
        YTPlayerView.initialize(YOUTUBE_EMBEDDED_VIDEO_API_KEY, object: YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                p2: Boolean
            ){
                player?.cueVideo(youtubeKey)
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Log.i("DetailsActivity", "onInitializationFailure")
            }
        })
    }
}
