package com.example.hotfix

import android.app.Application
import android.content.Context

/**
 * Created by cnting on 2022/8/8
 *
 */
class App : Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)

        //存在补丁包
        if(PatchUtil.hasPatch(base)){
            PatchUtil.loadPatch(base)
        }
    }
}