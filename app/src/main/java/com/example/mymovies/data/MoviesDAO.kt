package com.example.mymovies.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface MoviesDAO {
    @Query("select * from movies")
    fun getAllMovies():LiveData<List<Movie>>

    @Query("select * from movies where id == :movieId")
    fun getMovieById(movieId:Int):Movie

    @Query("delete from movies")
    fun deleteAllMovies()

    @Insert
    fun insertMovie(movie:Movie)

    @Delete
    fun deleteMovie(movie: Movie)
}