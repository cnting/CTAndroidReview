package com.example.websocket.server

import android.util.Log
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

/**
 * Created by cnting on 2022/6/21
 *
 */
class MockServer {
    fun mockServer(): String {
        val server = MockWebServer()
        val response = MockResponse()
            .withWebSocketUpgrade(object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    super.onOpen(webSocket, response)
                    Log.e("Server", "===>服务器 onOpen()")
                    webSocket.send("我是服务器，你好啊")
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    super.onMessage(webSocket, text)
                    Log.e("Server", "===>服务器收到消息:$text")
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    super.onClosed(webSocket, code, reason)
                    Log.e("Server", "onClosed")
                }
            })
        server.enqueue(response)

        return "ws://${server.hostName}:${server.port}/"
    }
}