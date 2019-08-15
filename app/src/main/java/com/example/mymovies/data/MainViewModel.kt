package com.example.mymovies.data

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import android.util.Log

class MainViewModel(application: Application) : AndroidViewModel(application) {
    companion object{
        lateinit var database: MovieDatabase
        lateinit var moviesLiveData: LiveData<List<Movie>>
        lateinit var favouriteMoviesLiveData: LiveData<List<FavoriteMovie>>
    }

    init {
        database = MovieDatabase.getInstance(getApplication())!!
        moviesLiveData = database.moviesDao().getAllMovies()
        favouriteMoviesLiveData = database.moviesDao().getAllFavouriteMovies()

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

    fun insertFavouriteMovie(movie:FavoriteMovie){
        InsertFavouriteMovieTask().execute(movie)
    }

    fun deleteFavouriteMovie(movie:FavoriteMovie){
        DeleteFavouriteMovieTask().execute(movie)
    }

    fun getFavouriteMovieById(id: Int): FavoriteMovie? {
        return GetFavouriteMovieTask().execute(id).get()
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

    private class InsertFavouriteMovieTask : AsyncTask<FavoriteMovie, Void, Void>() {
        override fun doInBackground(vararg params: FavoriteMovie?): Void ?{
            params[0]?.let {
                database.moviesDao().insertFavouriteMovie(it)
            }
            return null
        }
    }
    private class DeleteFavouriteMovieTask : AsyncTask<FavoriteMovie, Void, Void>() {
        override fun doInBackground(vararg params: FavoriteMovie?): Void? {
            params[0]?.let {
                database.moviesDao().deleteFavouriteMovie(it)
            }
            return null
        }
    }
    private class GetFavouriteMovieTask : AsyncTask<Int, Void, FavoriteMovie>() {
        override fun doInBackground(vararg params: Int?): FavoriteMovie? {
            var movie: FavoriteMovie? = null
            params[0]?.let {
                movie = database.moviesDao().getFavouriteMovieById(it)
            }
            return movie
        }
    }

/*
    //не нужно так как рум это делает за нас, в начале мы уже получаем обьект лайвДата
    private class GetAllFavouriteMovieTask : AsyncTask<Void, Void, LiveData<List<FavoriteMovie>>>() {
        override fun doInBackground(vararg params: Void?): LiveData<List<FavoriteMovie>>? {
            var favMovieLive:LiveData<List<FavoriteMovie>>? = null
            params[0]?.let{
                favMovieLive = database.moviesDao().getAllFavouriteMovies()
            }
            return favMovieLive
        }
    }*/

}
