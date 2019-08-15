package com.example.mymovies

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import com.example.mymovies.adapters.MovieAdapter
import com.example.mymovies.data.MainViewModel
import com.example.mymovies.data.Movie
import com.example.mymovies.utils.JSONUtils
import com.example.mymovies.utils.NetworkUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var movieAdapter: MovieAdapter
    lateinit var viewModel: MainViewModel

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
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        val movieLiveData = MainViewModel.moviesLiveData
        movieLiveData.observe(this, object : Observer<List<Movie>> {
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
            var movie:Movie = movieAdapter.getMovies().get(it)
            var intent: Intent = Intent(this, DeatailActivity::class.java)
            intent.putExtra("id", movie.id)
            startActivity(intent)
        }


        movieAdapter.setOnRichEndListener {
            // todo end rich
        }
        switchSort.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                val methodOfSort: Int
                if (isChecked) {
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

    fun switchOnMostRated(view: View) {
        switchSort.isChecked = true
        textViewMostPopular.setTextColor(Color.WHITE)
        textViewMostRaiting.setTextColor(resources.getColor(R.color.colorAccent))
    }

    fun switchMostPopular(view: View) {
        switchSort.isChecked = false
        textViewMostPopular.setTextColor(resources.getColor(R.color.colorAccent))
        textViewMostRaiting.setTextColor(Color.WHITE)
    }

    fun downloadData(methodOfSort: Int, page: Int) {
        val json = NetworkUtils.getJSONobject(methodOfSort, page)
        val movies: ArrayList<Movie>? = json?.let { JSONUtils.getMoviesFromJSON(it) }
        if (movies != null && movies.size != 0) {
            viewModel.deleteAllMovies()
            for (movie in movies) {
                viewModel.insertMovie(movie)
                Log.d("task", movie.posterPath+ " ")
            }
        }
    }


}



