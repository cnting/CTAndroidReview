package com.example.hotfix

import android.app.Application
import android.content.Context
import com.example.patch.PatchUtil

/**
 * Created by cnting on 2022/8/8
 *
 */
class App : Application() {
    companion object {
        const val patchName = "hotfix.dex"
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)

        //存在补丁包
        if (PatchUtil.hasPatch(base, patchName)) {
            PatchUtil.loadPatch(base, patchName)
        }
    }
}