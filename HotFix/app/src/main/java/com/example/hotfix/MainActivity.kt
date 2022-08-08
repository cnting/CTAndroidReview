package com.example.hotfix

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import dalvik.system.DexClassLoader
import java.io.File
import java.nio.file.Files

class MainActivity : AppCompatActivity() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val msgTv = findViewById<TextView>(R.id.tv)
        findViewById<View>(R.id.showMsgBtn).setOnClickListener {
            val clazz = classLoader.loadClass("com.example.hotfix.HelloAndroid")
            val iSayHello: ISayHello = clazz.newInstance() as ISayHello
            msgTv.text = iSayHello.say()
        }

        findViewById<View>(R.id.loadPatchBtn).setOnClickListener {
            PatchUtil.downloadPatch(this)
            Toast.makeText(this, "加载插件成功", Toast.LENGTH_SHORT).show()
        }
        findViewById<View>(R.id.deletePatchBtn).setOnClickListener {
            PatchUtil.deletePatch(this)
            Toast.makeText(this, "删除插件成功", Toast.LENGTH_SHORT).show()
        }

        findViewById<View>(R.id.killSelfBtn).setOnClickListener {
            android.os.Process.killProcess(android.os.Process.myPid())
        }
    }
}