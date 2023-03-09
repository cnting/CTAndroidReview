package com.cnting.jetpack

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by cnting on 2023/1/13
 *
 */
class MainViewModel : ViewModel() {
    private var num = 0
    val numLiveData = MutableLiveData<Int>()

    fun add() {
        numLiveData.postValue(++num)
    }
}