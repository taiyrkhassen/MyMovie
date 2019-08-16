package com.example.mymovies.utils

import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.content.AsyncTaskLoader
import android.util.Log
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class NetworkUtils {
    companion object {
        private var BASE_URL = "http://api.themoviedb.org/3/discover/movie"
        private var BASE_TRAILER_URL = "https://api.themoviedb.org/3/movie/%S/videos"
        private var BASE_REVIEWS_URL = "https://api.themoviedb.org/3/movie/%S/reviews"
        var BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v="

        var PARAMS_API_KEY = "api_key"
        var PARAMS_LANGUAGE = "language"
        var PARAMS_SORTED_BY = "sort_by"
        var PARAMS_PAGE = "page"
        var PARAMS_VOTE_COUNT = "vote_count.gte"

        var API_KEY = "bc0697c61cac9317cc0873d8477d1b07"
        var LANGUAGE = "ru-RU"
        var SORTED_BY_POPULARITY = "popularity.desc"
        var SORTED_BY_RATING = "vote_average.desc"
        var VOTE_COUNT = "500"
        var BY_POPULARITY = 0
        var BY_RATING = 1

        fun buildUrlReviews(id: Int): URL? {
            var result: URL? = null
            val url = String.format(BASE_REVIEWS_URL, id.toString())
            var uri = Uri.parse(url).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .build()
            result = URL(uri.toString())
            return result
        }

        fun buildUrlTrailer(id: Int): URL? {
            var result: URL? = null
            val url = String.format(BASE_TRAILER_URL, id.toString())
            val uri: Uri = Uri.parse(url).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE)
                .build()
            result = URL(uri.toString())
            return result
        }

        fun buildURL(sortBy: Int, page: Int): URL? {
            var result: URL? = null
            var sortHow: String? = null
            if (sortBy == BY_POPULARITY) {
                sortHow = SORTED_BY_POPULARITY
            } else if (sortBy == BY_RATING) {
                sortHow = SORTED_BY_RATING
            }

            val uri: Uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE)
                .appendQueryParameter(PARAMS_SORTED_BY, sortHow)
                .appendQueryParameter(PARAMS_PAGE, page.toString())
                .appendQueryParameter(PARAMS_VOTE_COUNT, VOTE_COUNT)
                .build()
            Log.d("url21", uri.toString())
            try {
                result = URL(uri.toString())
            } catch (exc: Exception) {
                exc.printStackTrace()
            }
            return result
        }
        //вместо асинк таск потомучто при перевороте он заного всегда загружает данные
        //а лоадер берет данные с бандла и использукет синглтон
        class JSONLoader : AsyncTaskLoader<JSONObject> {
            private var bundle:Bundle?
            lateinit var onStartLoadingListener:OnStartLoadingListener

            public interface OnStartLoadingListener{
                fun onStartLoading()
            }

            constructor(context: Context, bundle: Bundle?) : super(context) {
                this.bundle = bundle
            }

            override fun onStartLoading() {
                if(onStartLoadingListener!=null) {
                    super.onStartLoading()
                }
                forceLoad()
            }

            override fun loadInBackground(): JSONObject? {
                val urlAsString:String? = bundle!!.getString("url") ?: return null
                val url = URL(urlAsString)
                var result: JSONObject? = null
                if (url.toString().isEmpty()) {
                    return null
                }
                val stringBuilder: StringBuilder = StringBuilder()
                var conection: HttpURLConnection? = null
                try {
                    conection = url.openConnection() as HttpURLConnection
                    val inputStream2: InputStream = conection.inputStream
                    val inputStreamReader = InputStreamReader(inputStream2)
                    val bufferedReader = BufferedReader(inputStreamReader)
                    var line = bufferedReader.readLine()
                    while (line != null) {
                        stringBuilder.append(line)
                        line = bufferedReader.readLine()
                    }
                    result = JSONObject(stringBuilder.toString())
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    Log.d("mmm", "connection")
                } finally {
                    conection?.disconnect()
                }
                return result
            }

        }


        private class JSONLoadTask : AsyncTask<URL, Void, JSONObject>() {
            override fun doInBackground(vararg urls: URL?): JSONObject? {
                var result: JSONObject? = null
                if (urls.size == 0) {
                    return null
                }
                val stringBuilder: StringBuilder = StringBuilder()
                var conection: HttpURLConnection? = null
                try {
                    conection = urls[0]!!.openConnection() as HttpURLConnection
                    val inputStream2: InputStream = conection.inputStream
                    val inputStreamReader = InputStreamReader(inputStream2)
                    val bufferedReader = BufferedReader(inputStreamReader)
                    var line = bufferedReader.readLine()
                    while (line != null) {
                        stringBuilder.append(line)
                        line = bufferedReader.readLine()
                    }
                    result = JSONObject(stringBuilder.toString())
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    Log.d("mmm", "connection")
                } finally {
                    conection?.disconnect()
                }
                return result
            }
        }

        fun getJSONobject(sortBy: Int, page: Int): JSONObject? {
            val url = buildURL(sortBy, page)
            return JSONLoadTask().execute(url).get()
        }

        fun getJSONobjectVideo(id: Int): JSONObject {
            val url = buildUrlTrailer(id)
            return JSONLoadTask().execute(url).get()
        }

        fun getJSONobjectReviews(id: Int): JSONObject {
            val url = buildUrlReviews(id)
            return JSONLoadTask().execute(url).get()
        }
    }


}


//http://api.themoviedb.org/3/discover/movie?api_key=bc0697c61cac9317cc0873d8477d1b07&language=ru-RU&sort_by=popularity.desc&page=2
//https://api.themoviedb.org/3/discover/movie?api_key=bc0697c61cac9317cc0873d8477d1b07&language=ru-RU&sort_by=popularity.desc&page=2