package com.example.asmdemo.runtime_hook

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import de.robv.android.xposed.DexposedBridge
import de.robv.android.xposed.XC_MethodHook
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder

/**
 * Created by cnting on 2022/8/1
 * 运行时隐私政策检测
 */
class HookPrivacyMethod {

    fun hook(context: Context) {
        val inputStream = context.resources.assets.open("privacy_method.json")
        val reader = BufferedReader(InputStreamReader(inputStream))
        val result = StringBuilder()
        var line: String? = ""
        while (reader.readLine().also { line = it } != null) {
            result.append(line)
        }
        val config = Gson().fromJson(result.toString(), PrivacyMethod::class.java)
        config.methods.forEach {
            hookPrivacyMethod(it)
        }
    }

    private fun hookPrivacyMethod(data: PrivacyMethodData) {
        if (data.name_regex.isNotEmpty()) {
            val methodName = data.name_regex.substring(data.name_regex.lastIndexOf(".") + 1)
            val className = data.name_regex.substring(0, data.name_regex.lastIndexOf("."))
            try {
                val lintClass = Class.forName(className)
                DexposedBridge.hookAllMethods(lintClass, methodName, object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam?) {
                        super.beforeHookedMethod(param)
                        Log.i("===>", "beforeHookedMethod $className.$methodName，args:${param?.args?.contentToString()}")

                        Log.w("===>", "stack=" + Log.getStackTraceString(Throwable()))
                    }
                })
            } catch (e: Exception) {
                Log.w("===>", "hookPrivacyMethod:$className.$methodName,e=${e.message}")
            }

        }
    }
}