package com.example.mymovies

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.example.mymovies.adapters.MovieAdapter
import com.example.mymovies.data.FavoriteMovie
import com.example.mymovies.data.MainViewModel
import com.example.mymovies.data.Movie
import kotlinx.android.synthetic.main.activity_favorite.*

class FavoriteActivity : AppCompatActivity() {
    lateinit var movieAdapter: MovieAdapter
    lateinit var viewModel:MainViewModel

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val idMenu = item?.itemId
        when(idMenu){
            R.id.item_main -> startActivity(Intent(this, MainActivity::class.java))
            R.id.item_favourite -> startActivity(Intent(this, FavoriteActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        movieAdapter = MovieAdapter()
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        downloadFavourite()
        recyclerViewFavorite.adapter = movieAdapter
        recyclerViewFavorite.layoutManager = GridLayoutManager(this, 2)
        movieAdapter.setOnPosterClickListener {
            var movie: Movie = movieAdapter.getMovies().get(it)
            var intent: Intent = Intent(this, DeatailActivity::class.java)
            intent.putExtra("id", movie.id)
            startActivity(intent)
        }
    }

    fun downloadFavourite(){
        val favLive = MainViewModel.favouriteMoviesLiveData
        favLive.observe(this, object : (Observer<List<FavoriteMovie>>){
            override fun onChanged(favouriteMovies: List<FavoriteMovie>?) {
                movieAdapter.setMovies(favouriteMovies)
            }

        })

    }


}
