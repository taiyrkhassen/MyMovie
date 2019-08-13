package com.example.mymovies

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.GridLayout
import android.widget.Toast
import com.example.mymovies.data.MainViewModel
import com.example.mymovies.data.Movie
import com.example.mymovies.data.MovieDatabase
import com.example.mymovies.utils.JSONUtils
import com.example.mymovies.utils.NetworkUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var movieAdapter:MovieAdapter
    lateinit var viewModel:MainViewModel
    lateinit var database:MovieDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var numberOfPage:Int = 1
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        database = MovieDatabase.getInstance(this)!!
        var movieLiveData = MainViewModel.moviesLiveData
        movieLiveData.observe(this, object: Observer<List<Movie>>{
            override fun onChanged(movies: List<Movie>?) {
                movieAdapter.setMovies(movies)
            }

        })


        switchSort.isChecked = true
        recyclerViewPosters.layoutManager = GridLayoutManager(this, 2)
        movieAdapter = MovieAdapter()
        recyclerViewPosters.adapter = movieAdapter

       /* movieAdapter.setOnPosterClickListener(object: MovieAdapter.onPosterClickListener{
            override fun onClickItem(position: Int) {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }

        })*/

        movieAdapter.setOnPosterClickListener {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }
        movieAdapter.setOnRichEndListener {

        }
        switchSort.setOnCheckedChangeListener(object: CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                var methodOfSort:Int
                if(isChecked){
                    methodOfSort = NetworkUtils.BY_RATING
                    textViewMostPopular.setTextColor(Color.WHITE)
                    textViewMostRaiting.setTextColor(resources.getColor(R.color.colorAccent))
                } else {
                    methodOfSort = NetworkUtils.BY_POPULARITY
                    textViewMostPopular.setTextColor(resources.getColor(R.color.colorAccent))
                    textViewMostRaiting.setTextColor(Color.WHITE)
                }
                downloadData(methodOfSort, 2)
            }
        })
        switchSort.isChecked = false
    }
    fun switchOnMostRated(view: View){
        switchSort.isChecked = true
        textViewMostPopular.setTextColor(Color.WHITE)
        textViewMostRaiting.setTextColor(resources.getColor(R.color.colorAccent))
    }

    fun switchMostPopular(view: View){
        switchSort.isChecked = false
        textViewMostPopular.setTextColor(resources.getColor(R.color.colorAccent))
        textViewMostRaiting.setTextColor(Color.WHITE)
    }

    fun downloadData(methodOfSort:Int, page:Int){
        val json = NetworkUtils.getJSONobject(methodOfSort, page)
        val movies: ArrayList<Movie>? = json?.let { JSONUtils.getMoviesFromJSON(it) }
        if(movies!=null && movies.size!=0){
            viewModel.deleteAllMovies()
            for(movie in movies){
                viewModel.insertMovie(movie)
            }
        }

    }
    fun getAllMoviesFromDatabase(){

    }

}



