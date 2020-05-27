package com.plin.destinyreader.api

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import java.io.File

class Downloader(
    private val url: String,
    val onProgress: (readBytes: Long, totalBytes: Long) -> Unit
) {
    init {
        Fuel.download(url).fileDestination { response, Url ->
            Log.e("Response", response.toString())
            File.createTempFile("temp", ".tmp")
        }.progress { readBytes, totalBytes ->
            val progress = readBytes.toFloat() / totalBytes.toFloat()
            Log.w("Progress", progress.toString())
            onProgress(readBytes, totalBytes)

        }.response { request, response, result ->
            Log.w("Request", request.toString())
            Log.w("Response", response.toString())
            Log.w("Result", result.toString())
        }
    }
}