package com.cnting.recyclerview.util

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * Created by cnting on 2020/10/13
 *
 */
interface NetService {
    @GET("https://dog.ceo/api/breeds/image/random/20")
    suspend fun fetchImageUrl(): DogBean


}

object NetConfig {

    private val userAgentInterceptor = Interceptor { chain ->
        val request = chain.request()
        val agent = request.newBuilder()
            .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.2 (KHTML, like Gecko) Chrome/22.0.1216.0 Safari/537.2'")
            .build()
        chain.proceed(agent)
    }

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(userAgentInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://dog.ceo/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service: NetService = retrofit.create(NetService::class.java)


}
