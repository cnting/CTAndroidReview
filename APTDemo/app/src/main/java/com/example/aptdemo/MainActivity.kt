package com.example.aptdemo

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ioc_annotation.BindView

class MainActivity : AppCompatActivity() {

    @BindView(R.id.mainTextView)
    lateinit var textView: TextView

    @BindView(R.id.btn)
    lateinit var btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        com.example.ioc.ViewBinder.bind(this)
        Log.d("===>", "textView:$textView")
        Log.d("===>", "btn:$btn")
    }

}