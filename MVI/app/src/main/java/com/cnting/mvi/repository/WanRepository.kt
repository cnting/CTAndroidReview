package com.cnting.mvi.repository

import com.cnting.mvi.model.Article
import com.cnting.mvi.model.Banner
import com.cnting.mvi.model.BaseData
import com.cnting.mvi.net.WanRetrofitClient

/**
 * Created by cnting on 2023/4/17
 *
 */
class WanRepository : BaseRepository() {
    private val service = WanRetrofitClient.service

    suspend fun requestWanData(): BaseData<List<Banner>> {
        return executeRequest { service.getBanner() }
    }

    suspend fun requestRankData(page: Int): BaseData<Article> {
        return executeRequest { service.getArticle(page) }
    }
}