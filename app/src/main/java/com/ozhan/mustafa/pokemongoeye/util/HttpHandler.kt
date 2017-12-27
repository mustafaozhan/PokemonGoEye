package com.ozhan.mustafa.pokemongoeye.util

import android.util.Log
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.ProtocolException
import java.net.URL

/**
 * Created by Mustafa Ozhan on 12/27/17 at 1:50 PM on Arch Linux.
 */
class HttpHandler {
    companion object {

        private val TAG = HttpHandler::class.java.simpleName
    }

    fun makeServiceCall(reqUrl: String): String {
        var response: String? = null
        try {
            val url = URL(reqUrl)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            // read the response
            val `in` = BufferedInputStream(conn.inputStream)
            response = convertStreamToString(`in`)
        } catch (e: MalformedURLException) {
            Log.e(TAG, "MalformedURLException: " + e.message)
        } catch (e: ProtocolException) {
            Log.e(TAG, "ProtocolException: " + e.message)
        } catch (e: IOException) {
            Log.e(TAG, "IOException: " + e.message)
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.message)
        }

        return response!!
    }

    private fun convertStreamToString(`is`: InputStream): String {
        val reader = BufferedReader(InputStreamReader(`is`))
        val sb = StringBuilder()
        try {
            var line = reader.readLine()
            while (line != null) {
                line = reader.readLine()
                sb.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return sb.toString()
    }


}