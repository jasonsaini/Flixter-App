package com.example.flixter

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.flixter.databinding.ActivityMainBinding
import okhttp3.Headers
import org.json.JSONException

// url endpoint (The Movie Database API)
private const val UPCOMING_ENDPOINT_URL ="https://api.themoviedb.org/3/movie/upcoming?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"

class MainActivity : AppCompatActivity() {


    private val movies = mutableListOf<Movie>();
    private lateinit var moviesRecyclerView : RecyclerView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        moviesRecyclerView = findViewById(R.id.moviesRecyclerView)

        val movieAdapter = MovieAdapter(this, movies)
        moviesRecyclerView.adapter = movieAdapter
        moviesRecyclerView.layoutManager = LinearLayoutManager(this)

        // initialize asynchronous http client
        val asyncClient = AsyncHttpClient()
        asyncClient.get(UPCOMING_ENDPOINT_URL, object:JsonHttpResponseHandler(){
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response:String?,
                throwable: Throwable?
            )
            {
                Log.e("MainActivity", "onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i("MainActivity", "onSuccess: JSON data $json")
                try {
                    val movieJSONArr = json.jsonObject.getJSONArray("results")
                    movies.addAll(Movie.fromJSONArray(movieJSONArr))
                    movieAdapter.notifyDataSetChanged()
                    }
                catch(e:JSONException){
                    Log.i("MainActivity", "Encountered exception $e")
                }
            }
            })
        }
    }

