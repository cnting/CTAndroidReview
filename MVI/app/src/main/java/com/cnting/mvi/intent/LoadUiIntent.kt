package com.cnting.mvi.intent

/**
 * Created by cnting on 2023/4/17
 *
 */
sealed class LoadUiIntent : IUiIntent {
    data class Error(val errorMsg: String) : LoadUiIntent()
    data class Loading(val loading: Boolean) : LoadUiIntent()
    object ShowMainView : LoadUiIntent()
}