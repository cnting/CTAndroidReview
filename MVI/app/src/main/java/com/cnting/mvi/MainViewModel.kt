package com.cnting.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*

/**
 * Created by cnting on 2023/2/8
 *
 */
class MainViewModel : ViewModel() {
    private val _clickCountFlow = MutableStateFlow(0)
    val clickCountFlow = _clickCountFlow.asStateFlow()

    fun increaseClickCount() {
        _clickCountFlow.value += 1
    }

    private val timeFlow = flow {
        var time = 0
        while (true) {
            emit(time)
            kotlinx.coroutines.delay(1000)
            time++
        }
    }
    val stateFlow = timeFlow.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val _loginFlow = MutableStateFlow("")
    val loginFlow = _loginFlow.asSharedFlow()

    fun startLogin() {
        _loginFlow.value = "login success"
    }
}