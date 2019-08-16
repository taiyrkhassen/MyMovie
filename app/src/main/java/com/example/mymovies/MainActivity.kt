package com.example.mymovies

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.widget.GridLayoutManager
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.CompoundButton
import android.widget.Toast
import com.example.mymovies.adapters.MovieAdapter
import com.example.mymovies.data.MainViewModel
import com.example.mymovies.data.Movie
import com.example.mymovies.utils.JSONUtils
import com.example.mymovies.utils.NetworkUtils
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<JSONObject> {

    companion object {
        private final var LOADER_ID = 133
        private lateinit var loader: LoaderManager
        var pageCount = 1
        var methodOfSort: Int = 0
    }

    lateinit var movieAdapter: MovieAdapter
    lateinit var viewModel: MainViewModel
    var isLoaded = false

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val idMenu = item?.itemId
        when (idMenu) {
            R.id.item_main -> startActivity(Intent(this, MainActivity::class.java))
            R.id.item_favourite -> startActivity(Intent(this, FavoriteActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loader = LoaderManager.getInstance(this)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        val movieLiveData = MainViewModel.moviesLiveData
        movieLiveData.observe(this, object : Observer<List<Movie>> {
            override fun onChanged(movies: List<Movie>?) {
                if (pageCount == 1) {
                    movieAdapter.setMovies(movies);
                }

            }
        })


        switchSort.isChecked = true
        recyclerViewPosters.layoutManager = GridLayoutManager(this, getColumnCount())
        movieAdapter = MovieAdapter()
        recyclerViewPosters.adapter = movieAdapter

        /* movieAdapter.setOnPosterClickListener(object: MovieAdapter.onPosterClickListener{
             override fun onClickItem(position: Int) {
                 Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
             }

         })*/

        movieAdapter.setOnPosterClickListener {
            val movie: Movie = movieAdapter.getMovies().get(it)
            val intent: Intent = Intent(this, DeatailActivity::class.java)
            intent.putExtra("id", movie.id)
            startActivity(intent)
        }


        movieAdapter.setOnRichEndListener {
            if (!isLoaded) {
                downloadData(methodOfSort, pageCount)
            }
        }
        switchSort.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                pageCount = 1
                if (isChecked) {
                    methodOfSort = NetworkUtils.BY_RATING
                    textViewMostPopular.setTextColor(Color.WHITE)
                    textViewMostRaiting.setTextColor(resources.getColor(R.color.colorAccent))
                } else {
                    methodOfSort = NetworkUtils.BY_POPULARITY
                    textViewMostPopular.setTextColor(resources.getColor(R.color.colorAccent))
                    textViewMostRaiting.setTextColor(Color.WHITE)
                }
                downloadData(methodOfSort, pageCount)
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
        var url = NetworkUtils.buildURL(methodOfSort, page)
        var bundle = Bundle()
        bundle.putString("url", url.toString())
        loader.restartLoader(LOADER_ID, bundle, this)
    }

    override fun onCreateLoader(i: Int, bundle: Bundle?): Loader<JSONObject> {
        var jsonLoader = NetworkUtils.Companion.JSONLoader(this, bundle)
        showLoading()
        jsonLoader.onStartLoadingListener = object : NetworkUtils.Companion.JSONLoader.OnStartLoadingListener {
            override fun onStartLoading() {
                isLoaded = true
            }
        }

        return jsonLoader
    }


    override fun onLoadFinished(loaderL: Loader<JSONObject>, json: JSONObject?) {
        val movies: ArrayList<Movie>? = json?.let { JSONUtils.getMoviesFromJSON(it) }
        if (movies != null && movies.size != 0) {
            if (pageCount == 1) {
                viewModel.deleteAllMovies()
                movieAdapter.clear()
            }
            for (movie in movies) {
                viewModel.insertMovie(movie)
            }
            movieAdapter.addMovies(movies)
            pageCount++
        }
        loader.destroyLoader(LOADER_ID)
        isLoaded = false
        hideLoading()
    }

    override fun onLoaderReset(p0: Loader<JSONObject>) {

    }

    fun getColumnCount(): Int {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics) // getting dp
        val whidth = displayMetrics.widthPixels / displayMetrics.density.toInt()
        if ((whidth / 185) > 2) return whidth / 185
        else return 2
    }

    fun showLoading() {
        progress_bar.visibility = VISIBLE
    }

    fun hideLoading() {
        progress_bar.visibility = GONE
    }


//https://api.themoviedb.org/3/discover/movie?api_key=bc0697c61cac9317cc0873d8477d1b07&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&vote_count.gte=1000
//http://api.themoviedb.org/3/discover/movie?api_key=bc0697c61cac9317cc0873d8477d1b07&language=ru-RU&sort_by=vote_average.desc&page=2&vote_average.gte=500
}



