package com.cnting.mvi.ui.main

import com.cnting.mvi.intent.*
import com.cnting.mvi.repository.WanRepository
import com.cnting.mvi.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.*

/**
 * Created by cnting on 2023/2/8
 *
 */
class MainViewModel : BaseViewModel<MainState, MainIntent>() {
    private val repo = WanRepository()

    override fun initUiState(): MainState {
        return MainState(BannerUiState.INIT, DetailUiState.INIT)
    }

    override fun handleIntent(intent: IUiIntent) {
        when (intent) {
            MainIntent.GetBanner -> {
                requestDataWithFlow(
                    showLoading = true,
                    request = { repo.requestWanData() },
                    successCallback = { data ->
                        sendUiState {
                            copy(
                                bannerUiState = BannerUiState.SUCCESS(data)
                            )
                        }
                    })
            }
            is MainIntent.GetDetail -> {
                requestDataWithFlow(showLoading = false,
                    request = { repo.requestRankData(intent.page) },
                    successCallback = { data ->
                        sendUiState {
                            copy(
                                detailUiState = DetailUiState.SUCCESS(
                                    data
                                )
                            )
                        }
                    }
                )
            }
        }
    }


}