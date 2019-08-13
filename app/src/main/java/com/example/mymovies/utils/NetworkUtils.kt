package com.example.mymovies.utils

import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class NetworkUtils {
    companion object {
        private var BASE_URL = "http://api.themoviedb.org/3/discover/movie"



        var PARAMS_API_KEY = "api_key"
        var PARAMS_LANGUAGE = "language"
        var PARAMS_SORTED_BY = "sort_by"
        var PARAMS_PAGE = "page"

        var API_KEY = "bc0697c61cac9317cc0873d8477d1b07"
        var LANGUAGE = "ru-RU"
        var SORTED_BY_POPULARITY = "popularity.desc"
        var SORTED_BY_RATING = "vote_average.desc"

        var BY_POPULARITY = 0
        var BY_RATING = 1
        fun buildURL(sortBy: Int, page: Int): URL? {
            var result: URL? = null
            var sortHow: String? = null
            if (sortBy == BY_POPULARITY) {
                sortHow = SORTED_BY_POPULARITY
            } else if (sortBy == BY_RATING) {
                sortHow = SORTED_BY_RATING
            }

            var uri: Uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE)
                .appendQueryParameter(PARAMS_SORTED_BY, sortHow)
                .appendQueryParameter(PARAMS_PAGE, page.toString())
                .build()
            try {
                result = URL(uri.toString())
            } catch (exc: Exception) {
                exc.printStackTrace()
            }
            return result
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
    }

}
//http://api.themoviedb.org/3/discover/movie?api_key=bc0697c61cac9317cc0873d8477d1b07&language=ru-RU&sort_by=popularity.desc&page=2
//https://api.themoviedb.org/3/discover/movie?api_key=bc0697c61cac9317cc0873d8477d1b07&language=ru-RU&sort_by=popularity.desc&page=2