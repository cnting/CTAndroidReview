package com.cnting.jetpack

import android.app.Application
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner

/**
 * Created by cnting on 2023/1/11
 *
 */
class App : Application() {
    private val processLifecycleObserver by lazy {
        object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                super.onStart(owner)
                Log.d("===>", "进入前台")
            }

            override fun onStop(owner: LifecycleOwner) {
                super.onStop(owner)
                Log.d("===>", "进入后台")
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(processLifecycleObserver)
    }

    companion object {
        val isForeground
            get() = ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
    }
}