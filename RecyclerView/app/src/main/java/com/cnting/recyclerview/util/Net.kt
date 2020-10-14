package com.cnting.recyclerview.util

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * Created by cnting on 2020/10/13
 *
 */
interface NetService {
    @GET("https://dog.ceo/api/breeds/image/random/20")
    suspend fun fetchImageUrl(): ImageBean


}

class NetConfig {

    companion object {
        private val retrofit = Retrofit.Builder()
            .baseUrl("https://dog.ceo/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: NetService = retrofit.create(NetService::class.java)
    }

}
