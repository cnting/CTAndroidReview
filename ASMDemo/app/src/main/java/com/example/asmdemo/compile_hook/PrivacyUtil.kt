package com.example.asmdemo.compile_hook

import android.app.ActivityManager
import com.example.asm_annotation.AsmMethodOpcodes
import com.example.asm_annotation.AsmMethodReplace

/**
 * Created by cnting on 2022/8/2
 * 拦截并替换成指定方法
 */
object PrivacyUtil {

    var isAgreePrivacy = false

    @JvmStatic
    @AsmMethodReplace(oriClass = ActivityManager::class, oriAccess = AsmMethodOpcodes.INVOKEVIRTUAL)
    fun getRunningAppProcesses(manager: ActivityManager): List<ActivityManager.RunningAppProcessInfo?> {
        //检查是否同意隐私政策，如果同意，返回原始方法；否则返回自定义内容
        return if(!isAgreePrivacy){
            emptyList()
        }else{
            manager.runningAppProcesses
        }
    }
}