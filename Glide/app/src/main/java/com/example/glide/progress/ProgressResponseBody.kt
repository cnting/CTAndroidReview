package com.example.glide.progress

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSource
import okio.ForwardingSource
import okio.buffer

/**
 * Created by cnting on 2022/9/9
 *
 */
class ProgressResponseBody(private val originResponseBody: ResponseBody, url: String) :
    ResponseBody() {
    private var mListener = ProgressInterceptor.getListener(url)
    private val bufferedSource = object : ForwardingSource(originResponseBody.source()) {
        private var totalBytesRead = 0L
        private var currentProgress = 0

        override fun read(sink: Buffer, byteCount: Long): Long {
            return super.read(sink, byteCount).apply {
                if (this == -1L) {
                    totalBytesRead = contentLength()
                } else {
                    totalBytesRead += this
                }
                val progress = (100F * totalBytesRead / contentLength()).toInt()
                if (progress != currentProgress) {
                    currentProgress = progress
                    mListener?.onProgress(currentProgress)
                }
                if (totalBytesRead == contentLength()) {
                    mListener = null
                }
            }
        }
    }.buffer()

    override fun contentLength(): Long {
        return originResponseBody.contentLength()
    }

    override fun contentType(): MediaType? {
        return originResponseBody.contentType()
    }

    override fun source(): BufferedSource {
        return bufferedSource
    }


}