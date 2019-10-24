package com.example.mymovies.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.example.mymovies.FavoriteActivity

@Dao
interface MoviesDAO {
    @Query("select * from movies")
    fun getAllMovies():LiveData<List<Movie>>

    @Query("select * from favourite_movies")
    fun getAllFavouriteMovies():LiveData<List<FavoriteMovie>>

    @Query("select * from movies where id == :movieId")
    fun getMovieById(movieId:Int):Movie

    @Query("delete from movies")
    fun deleteAllMovies()

    @Insert
    fun insertMovie(movie:Movie)

    @Delete
    fun deleteMovie(movie: Movie)

    @Insert
    fun insertFavouriteMovie(fav_movie: FavoriteMovie)

    @Query("delete from favourite_movies where id == :fav_movie_id")
    fun deleteFavouriteMovie(fav_movie_id: Int)

    @Query("select * from favourite_movies where id == :favouriteMovieId")
    fun getFavouriteMovieById(favouriteMovieId:Int):FavoriteMovie
}