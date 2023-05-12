package com.cnting.okio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import okio.Okio
import okio.buffer
import okio.sink
import okio.source
import java.io.File
import java.nio.file.Files
import java.util.LinkedList

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun write(){
        val sink = File("test.txt").sink()
        val bufferedSink = sink.buffer()
        bufferedSink.writeUtf8("write \n sussssssss")
    }

    fun read(){
        val source = File("test.txt").source().buffer()
        source.readUtf8Line()
    }
}