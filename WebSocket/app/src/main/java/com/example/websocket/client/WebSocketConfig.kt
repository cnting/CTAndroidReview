package com.example.websocket.client

import android.util.Log
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit

/**
 * Created by cnting on 2022/6/21
 *
 */
class WebSocketConfig {
    private var webSocket: WebSocket? = null
    fun init(webServerUrl: String) {
        val client = OkHttpClient.Builder()
            .pingInterval(10, TimeUnit.SECONDS)
            .build()
        val request = Request.Builder().url(webServerUrl).build()
        webSocket = client.newWebSocket(request, webSocketListener)
    }

    private val webSocketListener = object : WebSocketListener() {
        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            Log.i("===>", "onClosed")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
            Log.i("===>", "onClosing")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            Log.i("===>", "onFailure:$response")
            t.printStackTrace()
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            super.onMessage(webSocket, bytes)
            Log.i("===>", "onMessage")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            Log.i("===>", "客户端收到消息：$text")

            webSocket.send("我是客户端，你好啊")
        }

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            Log.i("===>", "onOpen")
        }
    }

    fun send(message: String) {
        webSocket?.send(message)
    }

    fun send(message: ByteString) {
        webSocket?.send(message)
    }

    fun disconnect(code: Int, reason: String) {
        webSocket?.close(code, reason)
    }
}