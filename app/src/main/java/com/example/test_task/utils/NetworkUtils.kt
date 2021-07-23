package com.example.test_task.utils

import android.net.Uri
import com.example.test_task.utils.Constants.Companion.GET_ALL_IDS_URL
import com.example.test_task.utils.Constants.Companion.GET_DATA_BY_ID_BASE_URL
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class NetworkUtils {

    companion object {
        fun generateAllIdsUrl(): URL {
            val builtUri = Uri.parse(GET_ALL_IDS_URL).buildUpon().build()

            return (URL(builtUri.toString()))
        }

        fun generateUrlById(id: Int): URL {
            val builtUri = Uri.parse(GET_DATA_BY_ID_BASE_URL + id.toString()).buildUpon().build()

            return (URL(builtUri.toString()))
        }


        suspend fun getResponseFromUrl(url: URL): String? {
            return suspendCoroutine { continuation ->
                Thread(kotlinx.coroutines.Runnable {
                    with(url.openConnection() as HttpURLConnection){
                        try {
                            continuation.resume(inputStream.bufferedReader().readText())
                        } catch (ex: Exception) {
                            continuation.resumeWithException(ex)
                        } finally {
                            disconnect()
                        }
                    }
                }).start()
            }
        }
    }
}