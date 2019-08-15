package com.example.mymovies.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore

@Entity(tableName = "favourite_movies")
class FavoriteMovie(
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