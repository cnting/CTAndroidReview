package com.example.breakpointdownload.download

import java.net.HttpURLConnection

/**
 * Created by cnting on 2022/6/20
 *
 */
interface IDownloadListener {
    /**
     * 取消下载
     */
    fun onCancel()

    /**
     * 下载失败
     */
    fun onFail()

    /**
     * 下载预处理,可通过HttpURLConnection获取文件长度
     */
    fun onPreDownload(connection: HttpURLConnection)

    /**
     * 下载监听
     */
    fun onProgress(currentLocation: Long)

    /**
     * 单一线程的结束位置
     */
    fun onChildComplete(threadId: Int)
    fun onChildProgress(threadId: Int, curLocation: Long, progress: Float)

    /**
     * 开始
     */
    fun onStart(startLocation: Long)

    /**
     * 子程恢复下载的位置
     */
    fun onChildResume(resumeLocation: Long)

    /**
     * 恢复位置
     */
    fun onResume(resumeLocation: Long)
    fun onStop(stopLocation: Long)
    fun onComplete()
}

open class DefaultDownloadListener:IDownloadListener{
    override fun onCancel() {
    }

    override fun onFail() {
    }

    override fun onPreDownload(connection: HttpURLConnection) {
    }

    override fun onProgress(currentLocation: Long) {
    }

    override fun onChildComplete(threadId: Int) {
    }

    override fun onChildProgress(threadId: Int, curLocation: Long, progress: Float) {
    }

    override fun onStart(startLocation: Long) {
    }

    override fun onChildResume(resumeLocation: Long) {
    }

    override fun onResume(resumeLocation: Long) {
    }

    override fun onStop(stopLocation: Long) {
    }

    override fun onComplete() {
    }

}