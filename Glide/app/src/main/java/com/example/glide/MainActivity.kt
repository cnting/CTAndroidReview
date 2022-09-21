package com.example.glide

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.glide.progress.GlideApp
import com.jakewharton.disklrucache.DiskLruCache
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val img1 = findViewById<ImageView>(R.id.img1)
        val img2 = findViewById<ImageView>(R.id.img2)
        val img3 = findViewById<ImageView>(R.id.img3)
//
//        val url =
//            "https://img0.baidu.com/it/u=4277531701,3295668924&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=379"
////            "https://www.baidu.com/img/bd_logo1.png"
//
//
//        val URL =
//            "https://th.bing.com/th/id/OIP.ruqqxRtabgT7ZHCu53rJqgHaEK?w=290&h=180&c=7&r=0&o=5&dpr=2&pid=1.7"
//        val URL2 = "https://www.baidu.com/img/bd_logo1.png"
//        val GIF_URL =
//            "https://www.bing.com/th/id/OGC.ae97ebec530e2740bde35fe38d377ad1?pid=1.7&rurl=https%3a%2f%2fuploadfile.huiyi8.com%2f2014%2f1107%2f20141107115106120.gif&ehk=lfO3XiAxOKejupDlkNqN5qQbFjEETLNWrk8fEhNtnio%3d"
//
//        GlideApp.with(this)
//            .load(GIF_URL)
//            .placeholder(R.color.teal_700)
//            .progress(this)
//            .diskCacheStrategy(DiskCacheStrategy.NONE)
//            .into(ProgressImageViewTarget(GIF_URL, img1))
//
//        GlideApp.with(this)
//            .load(URL2)
//            .placeholder(R.color.teal_700)
//            .diskCacheStrategy(DiskCacheStrategy.NONE)
//            .into(ProgressImageViewTarget(URL2, img2))
//
//        GlideApp.with(this)
//            .load(URL)
//            .placeholder(R.color.teal_700)
//            .progress(this)
//            .diskCacheStrategy(DiskCacheStrategy.NONE)
//            .into(ProgressImageViewTarget(URL, img3))

//        val lruCache = LruCache<String, Int>(5)
//        (0 until 5).forEach {
//            lruCache.put("k$it", it)
//        }
//        Log.d("===>", "all cache：${lruCache.snapshot()}")
//        lruCache.get("k3")
//        Log.d("===>", "get k3：${lruCache.snapshot()}")
//        lruCache.get("k4")
//        Log.d("===>", "get k4：${lruCache.snapshot()}")
//        lruCache.put("k10", 10)
//        Log.d("===>", "添加k10，超过容量：${lruCache.snapshot()}")

        val diskLruCache = DiskLruCache.open(File(cacheDir, "test"), 1, 1, 10 * 1024 * 1024)
        val url =
            "https://www.baidu.com/img/bd_logo1.png"
        val key = hashKeyForDisk(url)

        Log.d("===>", "key:$key")

        val snapshot = diskLruCache.get(key)
        if (snapshot != null) {
            Log.d("===>", "从本地加载")
            img1.setImageBitmap(BitmapFactory.decodeStream(snapshot.getInputStream(0)))
        } else {
            thread {
                val editor = diskLruCache.edit(key)
                //保存
                val outputStream = editor.newOutputStream(0)
                if (downloadUrlToStream(url, outputStream)) {
                    editor.commit()
                } else {
                    editor.abort()
                }
                diskLruCache.flush()
            }
        }
    }


    fun hashKeyForDisk(key: String): String? {
        val cacheKey: String? = try {
            val mDigest: MessageDigest = MessageDigest.getInstance("MD5")
            mDigest.update(key.toByteArray())
            bytesToHexString(mDigest.digest())
        } catch (e: NoSuchAlgorithmException) {
            key.hashCode().toString()
        }
        return cacheKey
    }

    private fun bytesToHexString(bytes: ByteArray): String? {
        val sb = StringBuilder()
        for (i in bytes.indices) {
            val hex = Integer.toHexString(0xFF and bytes[i].toInt())
            if (hex.length == 1) {
                sb.append('0')
            }
            sb.append(hex)
        }
        return sb.toString()
    }

    private fun downloadUrlToStream(urlString: String, outputStream: OutputStream): Boolean {
        var urlConnection: HttpURLConnection? = null
        var out: BufferedOutputStream? = null
        var `in`: BufferedInputStream? = null
        try {
            val url = URL(urlString)
            urlConnection = url.openConnection() as HttpURLConnection
            `in` = BufferedInputStream(urlConnection.getInputStream(), 8 * 1024)
            out = BufferedOutputStream(outputStream, 8 * 1024)
            var b: Int
            while (`in`.read().also { b = it } != -1) {
                out.write(b)
            }
            return true
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            urlConnection?.disconnect()
            try {
                out?.close()
                `in`?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return false
    }
}