package com.cnting.bitmap.bigimg.decoder

import android.content.Context
import android.graphics.*
import android.os.Build
import java.io.InputStream
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantReadWriteLock

/**
 * Created by cnting on 2022/9/20
 *
 */
class SkiaImageRegionDecoder : ImageRegionDecoder {

    private var decoder: BitmapRegionDecoder? = null
    private val bitmapConfig = Bitmap.Config.RGB_565
    private val decoderLock = ReentrantReadWriteLock(true)

    override fun init(context: Context, inputStream: InputStream): Point {
        decoder = BitmapRegionDecoder.newInstance(inputStream, false)
        //返回 bitmap 长宽
        return Point(decoder!!.width, decoder!!.height)
    }

    override fun decodeRegion(rect: Rect, sampleSize: Int): Bitmap {
        getDecodeLock().lock()
        try {
            if (isReady()) {
                val options = BitmapFactory.Options()
                options.inSampleSize = sampleSize
                options.inPreferredConfig = bitmapConfig
                return decoder?.decodeRegion(rect, options)
                    ?: throw RuntimeException("Skia image decoder returned null bitmap - image format may not be supported")
            } else {
                throw IllegalStateException("Cannot decode region after decoder has been recycled")
            }
        } finally {
            //需要手动释放锁，放在finally比较保险
            getDecodeLock().unlock()
        }
    }

    override fun isReady(): Boolean {
        return decoder != null && decoder?.isRecycled != true
    }

    override fun recycle() {
        decoderLock.writeLock().lock()
        try {
            decoder?.recycle()
            decoder = null
        } finally {
            decoderLock.writeLock().unlock()
        }
    }

    /**
     * Before SDK 21, BitmapRegionDecoder was not synchronized internally. Any attempt to decode
     * regions from multiple threads with one decoder instance causes a segfault. For old versions
     * use the write lock to enforce single threaded decoding.
     *
     * ReadLock：多个线程可同时读
     * WriteLock：后面的读写都会被阻塞，写锁释放后，所有操作继续执行
     */
    private fun getDecodeLock(): Lock {
        return if (Build.VERSION.SDK_INT < 21) {
            decoderLock.writeLock()
        } else {
            decoderLock.readLock()
        }
    }
}