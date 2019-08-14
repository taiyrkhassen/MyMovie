package com.example.mymovies

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.mymovies.data.FavoriteMovie
import com.example.mymovies.data.MainViewModel
import com.example.mymovies.data.Movie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_deatail.*

class DeatailActivity : AppCompatActivity() {
    var movieId: Int = 0
    lateinit var viewModel: MainViewModel
    lateinit var movie: Movie
    var favoriteMovie: FavoriteMovie? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deatail)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        var intent = intent
        if (intent != null && intent.hasExtra("id")) {
            movieId = intent.getIntExtra("id", 0)
        } else {
            finish()
        }
        movie = viewModel.getMovieById(movieId)!!
        Picasso.get().load(movie?.bigPosterPath).into(imageViewPosterImage)
        textViewDate.setText(movie?.releaseDate)
        textViewOriginal.setText(movie?.originalTitle)
        textViewRating.setText(movie?.voteAverage.toString())
        textViewReview.setText(movie?.overView)
        textViewTitle.setText(movie?.title)
        setStarImage()
        val favouriteMovieLiveData = MainViewModel.favouriteMoviesLiveData
        favouriteMovieLiveData.observe(this, Observer<List<FavoriteMovie>> {

        })

    }

    fun changeFavourite(view: View) {
        favoriteMovie = viewModel.getFavouriteMovieById(movieId)
        if (favoriteMovie == null) {
            Toast.makeText(this, "Добавлено в избранное", Toast.LENGTH_SHORT).show()
            favoriteMovie?.let { viewModel.insertFavouriteMovie(FavoriteMovie(movie)) }
        } else {
            favoriteMovie?.let { viewModel.deleteFavouriteMovie(it) }
            Toast.makeText(this, "Удалено из избранных", Toast.LENGTH_SHORT).show()
        }
        setStarImage()
    }

    fun setStarImage() {
        favoriteMovie = viewModel.getFavouriteMovieById(movieId)
        Log.d("MYTAG", ""+favoriteMovie)
        if (favoriteMovie == null) {
            imageViewFavorite.setImageResource(R.drawable.diasbled)
        } else {
            imageViewFavorite.setImageResource(R.drawable.activated)
        }
    }
}
