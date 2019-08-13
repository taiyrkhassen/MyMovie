package com.example.mymovies.data

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.os.AsyncTask

class MainViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        lateinit var database: MovieDatabase
        lateinit var moviesLiveData: LiveData<List<Movie>>
    }

    init {
        database = MovieDatabase.getInstance(getApplication())!!
        moviesLiveData = database.moviesDao().getAllMovies()
    }

    fun getMovieById(id: Int): Movie? {
        return GetMovieTask().execute(id).get()
    }

    fun insertMovie(movie:Movie){
        InsertMovieTask().execute(movie)
    }
    fun deleteAllMovies(){
        DeleteAllTask().execute()
    }

    fun deleteMovie(movie:Movie){
        DeleteMovieTask().execute(movie)
    }

    private class GetMovieTask : AsyncTask<Int, Void, Movie>() {
        override fun doInBackground(vararg params: Int?): Movie? {
            var movie: Movie? = null
            params[0]?.let {
                movie = database.moviesDao().getMovieById(it)
            }
            return movie
        }
    }

    private class InsertMovieTask : AsyncTask<Movie, Void, Void>() {
        override fun doInBackground(vararg params: Movie?): Void? {
            params[0]?.let {
                database.moviesDao().insertMovie(it)
            }
            return null
        }
    }
    private class DeleteAllTask : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            database.moviesDao().deleteAllMovies()
            return null
        }

    }
    private class DeleteMovieTask : AsyncTask<Movie, Void, Void>() {
        override fun doInBackground(vararg params: Movie?): Void? {
            params[0]?.let {
                database.moviesDao().deleteMovie(it)
            }
            return null
        }
    }

}
