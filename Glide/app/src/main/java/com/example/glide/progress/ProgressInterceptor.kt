package com.example.glide.progress

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by cnting on 2022/9/9
 *
 */
class ProgressInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val url = request.url.toString()
        val responseBody = response.body ?: return response
        return response.newBuilder().body(ProgressResponseBody(responseBody, url)).build()
    }

    companion object {
        private val listeners = hashMapOf<String, OnProgressChangeListener>()
        fun addListener(url: String, onProgressChangeListener: OnProgressChangeListener) {
            listeners[url] = onProgressChangeListener
        }

        fun removeListener(url: String) {
            listeners.remove(url)
        }

        fun getListener(url: String) = listeners[url]
    }
}

interface OnProgressChangeListener {
    fun onProgress(progress: Int)
}