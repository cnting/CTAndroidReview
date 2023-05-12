package com.cnting.mvi.ui.main

import com.cnting.mvi.intent.IUiState
import com.cnting.mvi.model.Article
import com.cnting.mvi.model.Banner

/**
 * Created by cnting on 2023/4/17
 *
 */
data class MainState(val bannerUiState: BannerUiState, val detailUiState: DetailUiState) : IUiState

sealed class BannerUiState {
    object INIT : BannerUiState()
    data class SUCCESS(val models: List<Banner>) : BannerUiState()
}

sealed class DetailUiState {
    object INIT : DetailUiState()
    data class SUCCESS(val articles: Article) : DetailUiState()
}

