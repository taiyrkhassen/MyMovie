package com.example.mymovies.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "movies")
class Movie {
    @PrimaryKey
    var id: Int = 0;
    var voteCount: Int = 0;
    var title:String= ""
    var originalTitle:String = ""
    var overView:String = ""
    var posterPath:String = ""
    var voteAverage:Double = 0.0
    var releaseDate:String = ""
    var backdropPath:String = ""
    var bigPosterPath:String = ""
    constructor(
        id: Int,
        voteCount: Int,
        title: String,
        originalTitle: String,
        overView: String,
        posterPath: String,
        voteAverage: Double,
        releaseDate: String,
        backdropPath:String,
        bigPosterPath:String
    ) {
        this.id = id
        this.voteCount = voteCount
        this.title = title
        this.originalTitle = originalTitle
        this.overView = overView
        this.posterPath = posterPath
        this.voteAverage = voteAverage
        this.releaseDate = releaseDate
        this.backdropPath = backdropPath
        this.bigPosterPath = bigPosterPath
    }
}