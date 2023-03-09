package com.cnting.gradledemo

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cnting.apt_api.MyViewCreatorDelegate

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val str = "aaa"
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        val view = MyViewCreatorDelegate.getInstance().createView(name, context, attrs)
        if (view != null) return view
        return super.onCreateView(name, context, attrs)
    }
}