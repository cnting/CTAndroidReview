package com.cnting.mvi.repository

import com.cnting.mvi.model.BaseData
import com.cnting.mvi.model.ReqState

/**
 * Created by cnting on 2023/4/17
 *
 */
open class BaseRepository {
    suspend fun <T : Any> executeRequest(block: suspend () -> BaseData<T>): BaseData<T> {
        val baseData = block.invoke()
        if (baseData.code == 0) {
            baseData.state = ReqState.Success
        } else {
            baseData.state = ReqState.Error
        }
        return baseData
    }
}