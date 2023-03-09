package com.example.asmdemo

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.provider.Settings
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.asmdemo.compile_hook.PrivacyUtil
import com.example.asmdemo.runtime_hook.HookPrivacyMethod

class MainActivity : AppCompatActivity() {
    lateinit var tv: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        testRuntimeHook()


        tv = findViewById(R.id.tv)

        findViewById<CheckBox>(R.id.checkbox).setOnCheckedChangeListener { buttonView, isChecked ->
            PrivacyUtil.isAgreePrivacy = isChecked
            testCompileHook()
        }
    }

    /**
     * 运行时hook检测，测试在compileSdk 31上会报错
     */
    private fun testRuntimeHook() {
        HookPrivacyMethod().hook(this)

        val activityManager: ActivityManager =
            getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.runningAppProcesses

        val telephoneManager: TelephonyManager =
            getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        telephoneManager.listen(object : PhoneStateListener() {

        }, 0)

        val androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)
    }

    /**
     * 编译时hook
     */
    private fun testCompileHook() {
        val activityManager: ActivityManager =
            getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val result = activityManager.runningAppProcesses

        val androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)
        tv.text = "查询结果：${result.size}\n androidId:$androidId"
    }

}