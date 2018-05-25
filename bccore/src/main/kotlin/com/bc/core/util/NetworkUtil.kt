package com.bc.core.util

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object NetworkUtil {
    fun GET(urlApi: String, listener : (String) -> Unit) {
        val url = URL(urlApi)
        val httpURLConnection = url.openConnection() as HttpURLConnection
        val stringBuilder = StringBuilder()
        try {
            val bufferedReader = BufferedReader(InputStreamReader(httpURLConnection.inputStream))
            var line: String? = bufferedReader.readLine()
            while (line != null) {
                stringBuilder.append(line).append("\n")
                line = bufferedReader.readLine()
            }
            bufferedReader.close()
        } finally {
            httpURLConnection.disconnect()
        }
        listener.invoke(stringBuilder.toString())
    }
}