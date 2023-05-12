package com.cnting.mvi.ui.main

import com.cnting.mvi.intent.IUiIntent

/**
 * Created by cnting on 2023/4/17
 *
 */
sealed class MainIntent : IUiIntent {
    object GetBanner : MainIntent()
    data class GetDetail(val page: Int) : MainIntent()
}