package com.example.breakpointdownload.download

import java.io.File
import java.io.RandomAccessFile
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by cnting on 2022/6/20
 *
 */
class DownloadTask(
    private val downloadEntity: DownloadEntity,
    private val onComplete: (threadId: Int) -> Unit,
    private val onProgress: (threadId: Int, curLocation: Long, progress: Float) -> Unit
) : Runnable {

    private val configFilePath =
        downloadEntity.context.filesDir.path + File.separator + "temp" + File.separator + downloadEntity.tempFile.name + ".properties"

    override fun run() {
        val url = URL(downloadEntity.downloadUrl)
        val connection = url.openConnection() as HttpURLConnection
        //设置开始和结束位置
        connection.setRequestProperty(
            "Range",
            "bytes=${downloadEntity.startLocation}-${downloadEntity.endLocation}"
        )
        val inputStream = connection.inputStream
        val file = RandomAccessFile(downloadEntity.tempFile, "rwd")
        file.seek(downloadEntity.startLocation)
        var currentLocation = downloadEntity.startLocation
        val byteArray = ByteArray(1024)
        var bytesRead = inputStream.read(byteArray)
        while (bytesRead != -1) {
            file.write(byteArray, 0, bytesRead)
            currentLocation += bytesRead
            onProgress(
                downloadEntity.threadId,
                currentLocation,
                (currentLocation - downloadEntity.startLocation) * 1f / (downloadEntity.endLocation - downloadEntity.startLocation)
            )
            bytesRead = inputStream.read(byteArray)
        }
        onComplete(downloadEntity.threadId)
        file.close()
        inputStream.close()
    }
}