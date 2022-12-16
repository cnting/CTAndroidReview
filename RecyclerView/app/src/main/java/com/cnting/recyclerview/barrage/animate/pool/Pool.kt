package com.cnting.recyclerview.barrage.animate.pool

/**
 * Created by cnting on 2022/12/16
 *
 */
interface Pool<T> {
    //从池中获取对象
    fun acquire(): T?
    //释放对象
    fun release(instance: T):Boolean


}

class SimplePool<T>(maxPoolSize: Int) : Pool<T> {
    private var pool: MutableList<T?>
    private var poolSize = 0

    init {
        if (maxPoolSize <= 0) {
            throw IllegalArgumentException("The max pool size must be > 0")
        }
        pool = mutableListOf()
    }

    override fun acquire(): T? {
        if (poolSize > 0) {
            val lastPoolIndex = poolSize - 1
            val instance = pool[lastPoolIndex]
            pool[lastPoolIndex] = null
            poolSize--
            return instance
        }
        return null
    }

    override fun release(instance: T): Boolean {
        if(isInPool(instance)){
            throw IllegalStateException("Already in the pool")
        }
        if(poolSize<pool.size){
            pool[poolSize] = instance
            poolSize++
            return true
        }
        return false
    }

    private fun isInPool(instance: T): Boolean {
        (0 until poolSize).forEach { if (pool[it] == instance) return true }
        return false
    }
}