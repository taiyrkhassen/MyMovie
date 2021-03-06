package com.example.mymovies.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "favourite_movies")
class FavoriteMovie(
    uniqueId:Int,
    id: Int,
    voteCount: Int,
    title: String,
    originalTitle: String,
    overView: String,
    posterPath: String,
    voteAverage: Double,
    releaseDate: String,
    backdropPath: String,
    bigPosterPath: String
) : Movie(
    uniqueId,
    id,
    voteCount,
    title,
    originalTitle,
    overView,
    posterPath,
    voteAverage,
    releaseDate,
    backdropPath,
    bigPosterPath
) {

    @Ignore
    constructor(movie: Movie) : this(
        movie.uniqueId,
        movie.id,
        movie.voteCount,
        movie.title,
        movie.originalTitle,
        movie.overView,
        movie.posterPath,
        movie.voteAverage,
        movie.releaseDate,
        movie.backdropPath,
        movie.bigPosterPath
    ) {}

    override fun toString(): String {
        return title
    }

}


