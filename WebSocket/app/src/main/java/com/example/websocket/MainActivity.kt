package com.example.websocket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.websocket.client.WebSocketConfig
import com.example.websocket.server.MockServer
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val connectBtn = findViewById<View>(R.id.connectBtn)
        connectBtn.setOnClickListener {
            thread {
                val serverUrl = MockServer().mockServer()
                val webSocketConfig = WebSocketConfig()
                webSocketConfig.init(serverUrl)

            }
        }
    }
}