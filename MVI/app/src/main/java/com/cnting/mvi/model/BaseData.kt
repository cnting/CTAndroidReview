package com.cnting.mvi.model

import com.google.gson.annotations.SerializedName

/**
 * Created by cnting on 2023/4/17
 *
 */
class BaseData<T> {

    @SerializedName("errorCode")
    var code = -1

    @SerializedName("errorMsg")
    var msg: String? = null
    var data: T? = null
    var state: ReqState = ReqState.Error
    override fun toString(): String {
        return "BaseData(code=$code, msg=$msg, data=$data, state=$state)"
    }
}

enum class ReqState {
    Success, Error
}