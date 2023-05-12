package com.cnting.mvi.net

import com.cnting.mvi.model.Article
import com.cnting.mvi.model.Banner
import com.cnting.mvi.model.BaseData
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by cnting on 2023/4/17
 *
 */
interface WanApi {
 @GET("banner/json")
 suspend fun getBanner(): BaseData<List<Banner>>

 //页码从0开始
 @GET("article/list/{page}/json")
 suspend fun getArticle(@Path("page") page: Int): BaseData<Article>
}