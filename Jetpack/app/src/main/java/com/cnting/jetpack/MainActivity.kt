package com.cnting.jetpack

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn = findViewById<Button>(R.id.toBackgroundBtn)

        val viewModelProvider = ViewModelProvider(this)
        val vm = viewModelProvider.get(MainViewModel::class.java)
        vm.numLiveData.observe(this){
            Log.d("===>","num:$it")
        }
        btn.setOnClickListener {
            vm.add()
        }


    }
}