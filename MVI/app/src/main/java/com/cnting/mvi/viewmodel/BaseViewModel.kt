package com.cnting.mvi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cnting.mvi.intent.IUiIntent
import com.cnting.mvi.intent.IUiState
import com.cnting.mvi.intent.LoadUiIntent
import com.cnting.mvi.model.BaseData
import com.cnting.mvi.model.ReqState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by cnting on 2023/4/17
 *
 */
abstract class BaseViewModel<UiState : IUiState, UiIntent : IUiIntent> : ViewModel() {
    private val _uiStateFlow = MutableStateFlow(initUiState())
    val uiStateFlow: StateFlow<UiState> = _uiStateFlow

    private val _uiIntentFlow: Channel<UiIntent> = Channel()
    private val uiIntentFlow: Flow<UiIntent> = _uiIntentFlow.receiveAsFlow()

    private val _loadUiIntentFlow: Channel<LoadUiIntent> = Channel()
    val loadUiIntentFlow: Flow<LoadUiIntent> = _loadUiIntentFlow.receiveAsFlow()


    protected abstract fun initUiState(): UiState
    protected abstract fun handleIntent(intent: IUiIntent)

    init {
        viewModelScope.launch { uiIntentFlow.collect { handleIntent(it) } }
    }

    protected fun sendUiState(copy: UiState.() -> UiState) {
        _uiStateFlow.update { copy(_uiStateFlow.value) }
    }

    fun sendUiIntent(uiIntent: UiIntent) {
        viewModelScope.launch { _uiIntentFlow.send(uiIntent) }
    }

    private fun sendLoadUiIntent(loadUiIntent: LoadUiIntent) {
        viewModelScope.launch {
            _loadUiIntentFlow.send(loadUiIntent)
        }
    }

    protected fun <T : Any> requestDataWithFlow(
        showLoading: Boolean = true,
        request: suspend () -> BaseData<T>,
        successCallback: (T) -> Unit,
        failCallback: suspend (String) -> Unit = { errorMsg ->
            sendLoadUiIntent(
                LoadUiIntent.Error(
                    errorMsg
                )
            )
        }
    ) {
        viewModelScope.launch {
            if (showLoading) {
                sendLoadUiIntent(LoadUiIntent.Loading(true))
            }
            val baseData: BaseData<T>
            try {
                baseData = request()
                when (baseData.state) {
                    ReqState.Success -> {
                        sendLoadUiIntent(LoadUiIntent.ShowMainView)
                        baseData.data?.let { successCallback(it) }
                    }
                    ReqState.Error -> {
                        baseData.msg?.let { error(it) }
                    }
                }
            } catch (e: Exception) {
                e.message?.let { failCallback(it) }
            } finally {
                if (showLoading) {
                    sendLoadUiIntent(LoadUiIntent.Loading(false))
                }
            }
        }
    }
}