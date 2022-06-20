package com.example.breakpointdownload.download

import android.content.Context
import java.io.File

/**
 * Created by cnting on 2022/6/17
 *
 */
data class DownloadEntity(
    val context: Context,
    val fileSize: Long,
    val downloadUrl: String,
    var threadId: Int,
    var startLocation: Long,
    val endLocation: Long,
    val tempFile: File

)