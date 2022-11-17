package com.example.asmdemo.compile_hook

import android.app.ActivityManager
import android.content.ContentResolver
import android.provider.Settings
import android.util.Log
import androidx.annotation.Keep
import com.example.asm_annotation.AsmMethodOpcodes
import com.example.asm_annotation.AsmMethodReplace

/**
 * Created by cnting on 2022/8/2
 * 拦截并替换成指定方法
 */
@Keep
object PrivacyUtil {

    var isAgreePrivacy = false

    @JvmStatic
    @AsmMethodReplace(oriClass = ActivityManager::class, oriAccess = AsmMethodOpcodes.INVOKEVIRTUAL)
    fun getRunningAppProcesses(manager: ActivityManager): List<ActivityManager.RunningAppProcessInfo?> {
        Log.e("===>","调用这里")
        //检查是否同意隐私政策，如果同意，返回原始方法；否则返回自定义内容
        return if(!isAgreePrivacy){
            emptyList()
        }else{
            manager.runningAppProcesses
        }
    }

    /**
     * 读取AndroidId
     */
    @JvmStatic
    @AsmMethodReplace(oriClass = Settings.System::class, oriAccess = AsmMethodOpcodes.INVOKESTATIC)
    fun getString(resolver: ContentResolver, name: String): String? {
        Log.e("===>","AndroidID："+name)
        //处理AndroidId
        if (Settings.Secure.ANDROID_ID == name) {
            return ""
        }

        return Settings.System.getString(resolver, name)
    }
}