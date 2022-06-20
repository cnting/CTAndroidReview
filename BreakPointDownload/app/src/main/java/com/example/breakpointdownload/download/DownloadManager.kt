package com.example.breakpointdownload.download

import android.content.Context
import android.util.Log
import java.io.File
import java.io.RandomAccessFile
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

/**
 * Created by cnting on 2022/6/20
 *
 */
class DownloadManager {

    private val threadNum = 3

    fun download(
        context: Context,
        url: String,
        savePath: String,
        downloadListener: IDownloadListener
    ) {
        thread {
            val connection: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val contentLength = connection.contentLength
                val file = RandomAccessFile(savePath, "rwd")
                file.setLength(contentLength.toLong())
                downloadListener.onPreDownload(connection)
                val tempFile = File(savePath)
                val blockSize = contentLength / threadNum
                var completeNum = 0
                (0 until threadNum).forEach {
                    val startLocation = it * blockSize
                    val endLocation = (it + 1) * blockSize
                    val downloadEntity = DownloadEntity(
                        context,
                        contentLength.toLong(),
                        url,
                        it,
                        startLocation.toLong(),
                        endLocation.toLong(),
                        tempFile
                    )
                    val downloadTask = DownloadTask(downloadEntity, onComplete = { threadId ->
                        downloadListener.onChildComplete(threadId)
                        completeNum++
                        if (completeNum == threadNum) {
                            downloadListener.onComplete()
                        }
                    }, onProgress = { threadId, curLocation, progress ->
                        downloadListener.onChildProgress(threadId, curLocation, progress)
                    })
                    Thread(downloadTask).start()
                }
            } else {
                downloadListener.onFail()
                Log.e("===>", "download error:${connection.responseCode}")
            }
            connection.disconnect()
        }
    }
}