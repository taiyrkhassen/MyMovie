package com.example.mymovies

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.example.mymovies.adapters.ReviewAdapter
import com.example.mymovies.adapters.TraillerAdapter
import com.example.mymovies.data.FavoriteMovie
import com.example.mymovies.data.MainViewModel
import com.example.mymovies.data.Movie
import com.example.mymovies.utils.JSONUtils
import com.example.mymovies.utils.NetworkUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_deatail.*

class DeatailActivity : AppCompatActivity() {
    var movieId: Int = 0
    lateinit var viewModel: MainViewModel
    lateinit var movie: Movie
    var favoriteMovie: FavoriteMovie? = null
    lateinit var trailerAdapter:TraillerAdapter
    lateinit var reviewAdapter: ReviewAdapter
    var imageStar:ImageView?= null

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
        setContentView(R.layout.activity_deatail)

        imageStar = findViewById(R.id.imageViewFavorite)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        var intent = intent
        if (intent != null && intent.hasExtra("id")) {
            movieId = intent.getIntExtra("id", 0)
        } else {
            finish()
        }
        movie = viewModel.getMovieById(movieId)!!
        Picasso.get().load(movie.bigPosterPath).into(imageViewPosterImage)
        textViewDate.setText(movie.releaseDate)
        textViewOriginal.setText(movie.originalTitle)
        textViewRating.setText(movie.voteAverage.toString())
        textViewReview.setText(movie.overView)
        textViewTitle.setText(movie.title)
        val arrayReviews = JSONUtils.getReviewsFromJSON(NetworkUtils.getJSONobjectReviews(movieId))
        val arrayTrailers = JSONUtils.getVideosFromJSON(NetworkUtils.getJSONobjectVideo(movieId))
        reviewAdapter = ReviewAdapter()
        trailerAdapter = TraillerAdapter()
        reviewAdapter.setReviews(arrayReviews)
        trailerAdapter.setTrailers(arrayTrailers)
        setStarImage()
        trailerAdapter.setClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
        }


        recyclerViewTrailers.layoutManager = LinearLayoutManager(this)
        recyclerViewReviewss.layoutManager = LinearLayoutManager(this)
        recyclerViewTrailers.adapter = trailerAdapter
        recyclerViewReviewss.adapter = reviewAdapter

        imageStar!!.setOnClickListener{
            changeFavourite()
        }
    }

    fun changeFavourite() {
        favoriteMovie = viewModel.getFavouriteMovieById(movieId)
        if (favoriteMovie == null) {
            Toast.makeText(this, "Добавлено в избранное", Toast.LENGTH_SHORT).show()
            viewModel.insertFavouriteMovie(FavoriteMovie(movie))

        } else {
            viewModel.deleteFavouriteMovie(favoriteMovie!!)
            Toast.makeText(this, "Удалено из избранных", Toast.LENGTH_SHORT).show()
        }
        setStarImage()

    }

    fun setStarImage() {
        favoriteMovie = viewModel.getFavouriteMovieById(movieId)
        if (favoriteMovie == null) {
            imageViewFavorite.setImageResource(R.drawable.diasbled)
        } else {
            imageViewFavorite.setImageResource(R.drawable.activated)
        }
    }
}
