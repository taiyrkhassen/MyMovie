package com.example.mymovies.utils

import android.util.Log
import com.example.mymovies.data.Movie
import com.example.mymovies.data.Review
import com.example.mymovies.data.Video
import org.json.JSONObject
import java.lang.Exception

class JSONUtils {


    companion object {
        var KEY_RESULT: String = "results"

        //для отзывов
        var KEY_AUTHOR: String = "author"
        var KEY_CONTENT: String = "content"

        //для видео
        var KEY_KEY_OF_VIDEO: String = "key"
        var KEY_NAME: String = "name"

        //вся информация о фильме
        var KEY_VOTE_COUNT: String = "vote_count"
        var KEY_ID: String = "id"
        var KEY_TITLE: String = "title"
        var KEY_ORIGINAL_TITLE: String = "original_title"
        var KEY_OVERVIEW: String = "overview"
        var KEY_POSTER_PATH: String = "poster_path"
        var KEY_BACKDROP_PATH: String = "backdrop_path"
        var KEY_VOTE_AVERAGE: String = "vote_average"
        var KEY_RELEASE_DATE: String = "release_date"

        private var BASE_POSTER_URL = "https://image.tmdb.org/t/p/"
        private var SMALL_POSTER_SIZE = "w185"
        private var BIG_POSTER_SIZE = "w780"


        fun getVideosFromJSON(jsonObject: JSONObject): ArrayList<Video> {
            val arrayVideos: ArrayList<Video> = ArrayList()
            try{
                val jsonArray = jsonObject.getJSONArray(KEY_RESULT)
                for(i in 0..jsonArray.length()){
                    val objectReview = jsonArray.getJSONObject(i)
                    val key = NetworkUtils.BASE_YOUTUBE_URL + objectReview.getString(KEY_KEY_OF_VIDEO)
                    val name = objectReview.getString(KEY_NAME)
                    val video = Video(key, name)
                    arrayVideos.add(video)
                }
            } catch (exc:Exception){
                exc.printStackTrace()
            }
            return arrayVideos
        }

        fun getReviewsFromJSON(jsonObject: JSONObject): ArrayList<Review> {
            val arrayReviews: ArrayList<Review> = ArrayList()
            try{
                val jsonArray = jsonObject.getJSONArray(KEY_RESULT)
                for(i in 0..jsonArray.length()){
                    val objectReview = jsonArray.getJSONObject(i)
                    val author = objectReview.getString(KEY_AUTHOR)
                    val content = objectReview.getString(KEY_CONTENT)
                    val review = Review(author, content)
                    arrayReviews.add(review)
                }
            } catch (exc:Exception){
                exc.printStackTrace()
            }
            return arrayReviews
        }


        fun getMoviesFromJSON(jsonObject: JSONObject): ArrayList<Movie> {
            var arrayMovies: ArrayList<Movie> = ArrayList()
            try {
                val jsonArray = jsonObject.getJSONArray(KEY_RESULT)
                for (i in 0..jsonArray.length()) {
                    val objectMovie = jsonArray.getJSONObject(i)
                    val id = objectMovie.getInt(KEY_ID)
                    val voteCount = objectMovie.getInt(KEY_VOTE_COUNT)
                    val title = objectMovie.getString(KEY_TITLE)
                    val original_title = objectMovie.getString(KEY_ORIGINAL_TITLE)
                    val overview = objectMovie.getString(KEY_OVERVIEW)
                    val poster_path = BASE_POSTER_URL + SMALL_POSTER_SIZE + objectMovie.getString(KEY_POSTER_PATH)
                    val big_poster_path = BASE_POSTER_URL + BIG_POSTER_SIZE + objectMovie.getString(KEY_POSTER_PATH)
                    val vote_average = objectMovie.getDouble(KEY_VOTE_AVERAGE)
                    val releaseDate = objectMovie.getString(KEY_RELEASE_DATE)
                    val backdrop_path = objectMovie.getString(KEY_BACKDROP_PATH)
                    val movie = Movie(
                        id, voteCount, title, original_title, overview, poster_path, vote_average,
                        releaseDate, backdrop_path, big_poster_path
                    )
                    arrayMovies.add(movie)
                }

            } catch (exc: Throwable) {
                exc.printStackTrace()
                Log.d("download", exc.message)
            }

            return arrayMovies
        }
    }

}