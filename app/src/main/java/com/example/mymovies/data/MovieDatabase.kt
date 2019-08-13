package com.example.mymovies.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [Movie::class],  version = 1 , exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    companion object {
        private var movieDatabase: MovieDatabase? = null
        private val DB_NAME = "movies.db"
        private val LOCK = Any()
        fun getInstance(context: Context): MovieDatabase? {
            synchronized(LOCK) {
                if (movieDatabase == null) {
                    movieDatabase = Room.databaseBuilder(context, MovieDatabase::class.java, DB_NAME).build()
                }
            }
            return movieDatabase
        }
    }

    abstract fun moviesDao(): MoviesDAO
}