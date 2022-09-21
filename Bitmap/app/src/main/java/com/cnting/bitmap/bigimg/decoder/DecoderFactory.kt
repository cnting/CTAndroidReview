package com.cnting.bitmap.bigimg.decoder

/**
 * Created by cnting on 2022/9/20
 *
 */
class DecoderFactory<T : ImageRegionDecoder>(private val clazz: Class<out T>) {
    fun make(): T {
        return clazz.newInstance()
    }
}