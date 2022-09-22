package com.cnting.bitmap.bigimg

import android.content.Context
import android.graphics.*
import android.os.AsyncTask
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.cnting.bitmap.bigimg.bean.ImageResource
import com.cnting.bitmap.bigimg.bean.ScaleAndTranslate
import com.cnting.bitmap.bigimg.bean.Tile
import com.cnting.bitmap.bigimg.decoder.DecoderFactory
import com.cnting.bitmap.bigimg.decoder.ImageRegionDecoder
import com.cnting.bitmap.bigimg.decoder.SkiaImageRegionDecoder
import java.lang.ref.WeakReference
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.math.roundToInt

/**
 * Created by cnting on 2021/8/24
 * 参考 https://github.com/davemorrissey/subsampling-scale-image-view
 */
class BigImageView : View {

    //图片宽高
    private var imgWidth = 0
    private var imgHeight = 0
    private var imageRegionDecoder: ImageRegionDecoder? = null
    private var bitmapPaint: Paint? = null

    //不同采样率对应不同切片
    private val tileMap = mutableMapOf<Int, List<Tile>>()

    private val satTmp = ScaleAndTranslate(0f, PointF(0f, 0f))

    // 当前缩放
    private var scale = 0f
    private var scaleStart = 0f
    private var vDistStart = 0f

    // 当前偏移
    private val vTranslate = PointF(0f, 0f)
    private val vTranslateStart = PointF(0f, 0f)
    private val vTranslateBefore = PointF(0f, 0f)
    private val vCenterStart = PointF(0f, 0f)


    //显示完整图片时的采样率
    private var fullImageSampleSize = 0

    //切片最大宽高
    private var maxTileWidth = Integer.MAX_VALUE
    private var maxTileHeight = Integer.MAX_VALUE
    private var minTileDpi: Float = -1f

    //最大可放大倍数
    private val maxScale = 2f

    private var readySent = false

    //矩阵变换
    private val tileMatrix = Matrix()
    private val srcArray = FloatArray(8)
    private val dstArray = FloatArray(8)

    //延迟构建SkiaImageRegionDecoder
    private val regionDecoderFactory =
        DecoderFactory<ImageRegionDecoder>(SkiaImageRegionDecoder::class.java)

    private val decoderLock = ReentrantReadWriteLock(true)

    //手势
    private lateinit var detector: GestureDetector
    private lateinit var singleGestureDetector: GestureDetector

    //快速缩放
    private var isQuickScaling = false

    //两指缩放
    private var isZooming = false

    //一指平移
    private var isPanning = false

    //Max touches used in current gesture
    private var maxTouchCount = 0

    private var isFirstTimeFitBounds = true


    constructor(context: Context?) : super(context)

    constructor(
        context: Context,
        attrs: AttributeSet
    ) : super(context, attrs)

    constructor (
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    init {
        setMinTileDpi(320f)
        setGestureDetector()
    }

    /**
     * 设置图片资源
     */
    fun setImageSource(imageResource: ImageResource) {
        TileInitTask(this, context, regionDecoderFactory, imageResource).execute()
    }

    fun setMinTileDpi(minTileDpi: Float) {
        val metrics = resources.displayMetrics
        val averageDpi = (metrics.xdpi + metrics.ydpi) / 2
        this.minTileDpi = Math.min(minTileDpi, averageDpi)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)
        val resizeWidth = MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY
        val resizeHeight = MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY
        var width = parentWidth
        var height = parentHeight
        if (imgWidth > 0 && imgHeight > 0) {
            if (resizeWidth && resizeHeight) {
                width = imgWidth
                height = imgHeight
            } else if (resizeHeight) {
                height = (imgHeight.toFloat() / imgWidth * width).toInt()
            } else if (resizeWidth) {
                width = (imgWidth.toFloat() / imgHeight * height).toInt()
            }
        }
        width = width.coerceAtLeast(suggestedMinimumWidth)
        height = height.coerceAtLeast(suggestedMinimumHeight)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        createPaints()
        if (imgWidth == 0 || imgHeight == 0 || width == 0 || height == 0) return

        if (tileMap.isEmpty() && imageRegionDecoder != null) {
            initBaseLayer(getMaxBitmapDimension(canvas))
        }
        if (!checkReady()) return

        preDraw()

        if (tileMap.isNotEmpty() && isBaseLayerReady()) {
            val sampleSize = calculateInSampleSize(scale).coerceAtMost(fullImageSampleSize)

            //sampleSize对应的切片是否加载完
            var hasMissingTiles = false
            tileMap.forEach {
                if (it.key == sampleSize) {
                    it.value.forEach { tile ->
                        if (tile.visible && (tile.loading || tile.bitmap == null)) {
                            hasMissingTiles = true
                        }
                    }
                }
            }

            tileMap.forEach {
                //如果sampleSize对应的切片没加载完，就把老的切片也展示出来，避免显示空白
                if (it.key == sampleSize || hasMissingTiles) {
                    it.value.forEachIndexed { index, tile ->
                        sourceToViewRect(tile.sRect, tile.vRect)

                        if (!tile.loading && tile.bitmap != null) {
                            val bitmapWidth = tile.bitmap!!.width.toFloat()
                            val bitmapHeight = tile.bitmap!!.height.toFloat()
                            val vRect = tile.vRect

                            tileMatrix.reset()
                            setMatrixArray(
                                srcArray,
                                0f,
                                0f,
                                bitmapWidth,
                                0f,
                                bitmapWidth,
                                bitmapHeight,
                                0f,
                                bitmapHeight
                            )

                            setMatrixArray(
                                dstArray,
                                vRect.left,
                                vRect.top,
                                vRect.right,
                                vRect.top,
                                vRect.right,
                                vRect.bottom,
                                vRect.left,
                                vRect.bottom
                            )
                            //因为有可能是先显示老切片，所以要用矩阵转变换大小
                            tileMatrix.setPolyToPoly(srcArray, 0, dstArray, 0, 4)
                            canvas.drawBitmap(tile.bitmap!!, tileMatrix, bitmapPaint)
                        }
                    }
                }
            }

        }
    }

    private fun setGestureDetector() {
        detector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                performClick()
                return true
            }

            override fun onDoubleTap(e: MotionEvent?): Boolean {
                return super.onDoubleTap(e)
            }
        })

        singleGestureDetector =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                    performClick()
                    return true
                }
            })
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        //检测 flings、单击、双击
        if (!isQuickScaling && detector.onTouchEvent(event)) {
            isZooming = false
            isPanning = false
            maxTouchCount = 0
            return true
        }
        vTranslateBefore.set(vTranslate)

        val handled = onTouchEventInternal(event)

        return handled || super.onTouchEvent(event)
    }

    private fun onTouchEventInternal(event: MotionEvent): Boolean {
        val touchCount = event.pointerCount

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                requestDisallowInterceptTouchEvent(true)
                maxTouchCount = Math.max(maxTouchCount, touchCount)
                if (touchCount >= 2) {
                    //记录双指缩放 按下时的数据
                    //两指距离
                    val distance =
                        distance(event.getX(0), event.getX(1), event.getY(0), event.getY(1))
                    scaleStart = scale
                    vDistStart = distance
                    vTranslateStart.set(vTranslate)
                    //两指中心位置
                    vCenterStart.set(
                        (event.getX(0) + event.getX(1)) / 2,
                        (event.getY(0) + event.getY(1)) / 2,
                    )
                } else if (!isQuickScaling) {
                    vTranslateStart.set(vTranslate)
                    vCenterStart.set(event.x, event.y)
                }
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                var consumed = false
                if (maxTouchCount > 0) {
                    //双指缩放
                    if (touchCount >= 2) {
                        val vDistEnd =
                            distance(event.getX(0), event.getX(1), event.getY(0), event.getY(1))
                        //两个手指中心位置
                        val vCenterEndX = (event.getX(0) + event.getX(1)) / 2
                        val vCenterEndY = (event.getY(0) + event.getY(1)) / 2

                        //缩放距离超过5
                        if (distance(vCenterStart.x, vCenterEndX, vCenterStart.y, vCenterEndY) > 5
                            || Math.abs(vDistEnd - vDistStart) > 5
                            || isPanning
                        ) {
                            isZooming = true
                            isPanning = true
                            consumed = true

                            val previousScale = scale
                            scale = Math.min(maxScale, (vDistEnd / vDistStart) * scaleStart)

                            //已经缩放到最小，重新设置 开始值
                            if (scale <= minScale()) {
                                vDistStart = vDistEnd
                                scaleStart = minScale()
                                vCenterStart.set(vCenterEndX, vCenterEndY)
                                vTranslateStart.set(vTranslate)

                            } else {
                                //下面是以两根手指的中心位置 为中心 进行缩放

                                //缩放前，手指中心偏移距离
                                val vLeftStart = vCenterStart.x - vTranslateStart.x
                                val vTopStart = vCenterStart.y - vTranslateStart.y
                                //偏移距离也需要进行缩放
                                val vLeftNow = vLeftStart * (scale / scaleStart)
                                val vTopNow = vTopStart * (scale / scaleStart)
                                //比如一个点，放大时会往右上方偏移，我们需要把它减掉，回到左下方，也就是原来位置
                                vTranslate.x = vCenterEndX - vLeftNow
                                vTranslate.y = vCenterEndY - vTopNow


                                //放大超过当前屏幕，需要修正 scale 和 vTranslate
                                if ((previousScale * imgHeight < height && scale * imgHeight >= height)
                                    || (previousScale * imgWidth < width && scale * imgWidth >= width)
                                ) {
                                    fitToBounds(true)
                                    vCenterStart.set(vCenterEndX, vCenterEndY)
                                    vTranslateStart.set(vTranslate)
                                    scaleStart = scale
                                    vDistStart = vDistEnd
                                }
                            }
                            fitToBounds(true)
                            refreshRequireTiles()
                        }
                    }
                    if (consumed) {
                        invalidate()
                        return true
                    }
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                //ACTION_POINTER_UP，抬起手指的数据还在。比如两根手指，抬起一根，pointerCount还是2。之后的move事件拿到的pointerCount才是1
                if (maxTouchCount > 0 && (isZooming || isPanning)) {
                    if (isZooming && touchCount == 2) {
                        isPanning = true
                        vTranslateStart.set(vTranslate)

                        if (event.actionIndex == 1) {
                            //放开第1个手指，中心点变成 index 0
                            vCenterStart.set(event.getX(0), event.getY(0))
                        } else {
                            //放开第0个手指，中心点变成 index 1
                            vCenterStart.set(event.getX(1), event.getY(1))
                        }
                    }
                    //如果现在touchCount是<=2，抬起后变成<=1，所以不能缩放了
                    if (touchCount <= 2) {
                        isZooming = false
                    }
                    if (touchCount <= 1) {
                        isPanning = false
                        maxTouchCount = 0
                    }
                    refreshRequireTiles()
                    return true
                }
                if (touchCount == 1) {
                    isZooming = false
                    isPanning = false
                    maxTouchCount = 0
                }
                return true
            }
        }
        return false
    }

    private fun distance(x0: Float, x1: Float, y0: Float, y1: Float): Float {
        val x = x0 - x1
        val y = y0 - y1
        return Math.sqrt((x * x + y * y).toDouble()).toFloat()
    }

    private fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        parent?.requestDisallowInterceptTouchEvent(disallowIntercept)
    }

    /**
     * 左、上、右、上、右、下、左、下
     */
    private fun setMatrixArray(
        arr: FloatArray,
        f0: Float,
        f1: Float,
        f2: Float,
        f3: Float,
        f4: Float,
        f5: Float,
        f6: Float,
        f7: Float
    ) {
        arr[0] = f0
        arr[1] = f1
        arr[2] = f2
        arr[3] = f3
        arr[4] = f4
        arr[5] = f5
        arr[6] = f6
        arr[7] = f7
    }


    /**
     * 原始切片经过 缩放偏移后 的Rect
     */
    private fun sourceToViewRect(sRect: Rect, vTarget: RectF) {
        fun sourceToViewX(sx: Int): Float {
            return (sx * scale + vTranslate.x)
        }

        fun sourceToViewY(sy: Int): Float {
            return (sy * scale + vTranslate.y)
        }

        vTarget.set(
            sourceToViewX(sRect.left),
            sourceToViewY(sRect.top),
            sourceToViewX(sRect.right),
            sourceToViewY(sRect.bottom)
        )
    }

    private fun viewToSourceX(vx: Float): Float {
        return (vx - vTranslate.x) / scale
    }

    private fun viewToSourceY(vy: Float): Float {
        return (vy - vTranslate.y) / scale
    }


    private fun preDraw() {
        if (width == 0 || height == 0 || imgWidth <= 0 || imgHeight <= 0) return
        fitToBounds(false)
    }

    /**
     * 显示完整图叫做 baseLayer
     */
    private fun isBaseLayerReady(): Boolean {
        if (tileMap.isNotEmpty()) {
            var baseLayerReady = true
            tileMap.forEach {
                if (it.key == fullImageSampleSize) {
                    it.value.forEach { tile ->
                        if (tile.loading || tile.bitmap == null) {
                            baseLayerReady = false
                        }
                    }
                }
            }
            return baseLayerReady
        }
        return false
    }

    private fun checkReady(): Boolean {
        val ready =
            width > 0 && height > 0 && imgWidth > 0 && imgHeight > 0 && isBaseLayerReady()
        if (!readySent && ready) {
            readySent = true
        }
        return ready
    }

    /**
     * 初始化不同采样率下的切片，不同采样率对应不同切片
     */
    private fun initBaseLayer(maxTileDimensions: Point) {
        fullImageSampleSize = calculateInSampleSize(minScale())

        if (fullImageSampleSize == 1 && imgWidth < maxTileDimensions.x && imgHeight < maxTileDimensions.y) {
            imageRegionDecoder?.recycle()
            imageRegionDecoder = null
            //todo 加载完整图片

        } else {
            initTileMap(maxTileDimensions)
            val fullImgTiles = tileMap[fullImageSampleSize]
            fullImgTiles?.forEach { TileLoadTask(this, imageRegionDecoder!!, it).execute() }
        }
    }

    private fun initTileMap(maxTileDimensions: Point) {
        var sampleSize = fullImageSampleSize
        var xTiles = 1  //x轴分成几块
        var yTiles = 1  //y轴分成几块
        while (true) {
            //一个切片的宽高
            var tileWidth = imgWidth / xTiles
            var tileHeight = imgHeight / xTiles
            //采样后的切片宽高
            var subTileWidth = tileWidth / sampleSize
            var subTileHeight = tileHeight / sampleSize

            //调整subTileWidth的宽度，使其可以全部显示在屏幕上
            // TODO: 这里 subTileWidth + xTiles + 1 没看懂
            while (subTileWidth + xTiles + 1 > maxTileDimensions.x
                || (subTileWidth > width * 1.25 && sampleSize < fullImageSampleSize)
            ) {
                xTiles += 1
                tileWidth = imgWidth / xTiles
                subTileWidth = tileWidth / sampleSize
            }

            while (subTileHeight + yTiles + 1 > maxTileDimensions.y
                || (subTileHeight > height * 1.25 && sampleSize < fullImageSampleSize)
            ) {
                yTiles += 1
                tileHeight = imgHeight / yTiles
                subTileHeight = tileHeight / sampleSize
            }

            val tiles = mutableListOf<Tile>()
            (0 until xTiles).forEach { x ->
                (0 until yTiles).forEach { y ->
                    val tile =
                        Tile(
                            sampleSize = sampleSize,
                            visible = sampleSize == fullImageSampleSize, //默认展示完整图片，所以采样率为fullImageSampleSize是tile可见
                            sRect = Rect(
                                x * tileWidth, y * tileHeight,
                                if (x == xTiles - 1) imgWidth else (x + 1) * tileWidth,
                                if (y == yTiles - 1) imgHeight else (y + 1) * tileHeight
                            ),
                            vRect = RectF(0f, 0f, 0f, 0f),
                        )
                    tiles.add(tile)

                }
            }
            tileMap[sampleSize] = tiles

            if (sampleSize == 1) {
                break
            } else {
                sampleSize /= 2
            }
        }
    }

    private fun refreshRequireTiles() {
        if (imageRegionDecoder == null || tileMap.isEmpty()) return
        val mDecoder = imageRegionDecoder ?: return
        val sampleSize = calculateInSampleSize(scale).coerceAtMost(fullImageSampleSize)
        tileMap.forEach {
            it.value.forEach { tile ->
                //不清除 fullImageSampleSize 对应的 Bitmap
                if (tile.sampleSize < sampleSize || (tile.sampleSize > sampleSize && tile.sampleSize != fullImageSampleSize)) {
                    tile.visible = false
                    tile.bitmap?.recycle()
                    tile.bitmap = null
                }

                if (tile.sampleSize == sampleSize) {
                    if (tileVisible(tile)) {
                        tile.visible = true
                        if (!tile.loading && tile.bitmap == null) {
                            TileLoadTask(this, mDecoder, tile).execute()
                        }
                    } else if (tile.sampleSize != fullImageSampleSize) {
                        tile.visible = false
                        tile.bitmap?.recycle()
                        tile.bitmap = null
                    }
                } else if (tile.sampleSize == fullImageSampleSize) {
                    tile.visible = true
                }
            }
        }
    }

    private fun tileVisible(tile: Tile): Boolean {
        val sVisLeft = viewToSourceX(0f)
        val sVisRight = viewToSourceX(width.toFloat())
        val sVisTop = viewToSourceY(0f)
        val sVisBottom = viewToSourceY(height.toFloat())
        return !(tile.sRect.right < sVisLeft || tile.sRect.left > sVisRight || tile.sRect.bottom < sVisTop || tile.sRect.top > sVisBottom)
    }

    private fun fitToBounds(center: Boolean) {
        //satTmp存在的意义：避免在计算 scale 和 vTranslate 新值的过程中这两个变量在其他地方被改变
        satTmp.scale = scale
        satTmp.vTranslate.set(vTranslate)
        fitToBounds(center, satTmp)
        scale = satTmp.scale
        vTranslate.set(satTmp.vTranslate)

        //第一次加载，设置图片居中
        if (isFirstTimeFitBounds) {
            isFirstTimeFitBounds = false
            vTranslate.set(translateToCenter(scale))
        }
    }

    /**
     * 控制 scale 和 translate 不超出边界
     */
    private fun fitToBounds(center: Boolean, scaleAndTranslate: ScaleAndTranslate) {
        val scale = limitScale(scaleAndTranslate.scale)
        scaleAndTranslate.scale = scale

        val scaleWidth = scale * imgWidth
        val scaleHeight = scale * imgHeight
        val vTranslate = scaleAndTranslate.vTranslate

        if (center) {
            vTranslate.x = vTranslate.x
                .coerceAtLeast(-(width - scaleWidth) / 2)
                .coerceAtMost((width - scaleWidth) / 2)

            vTranslate.y = vTranslate.y
                .coerceAtLeast(-(height - scaleHeight) / 2)
                .coerceAtMost((height - scaleHeight) / 2)
        } else {
            vTranslate.x = vTranslate.x
                .coerceAtLeast(-scaleWidth)
                .coerceAtMost(width.toFloat())
            vTranslate.y = vTranslate.y.coerceAtLeast(-scaleHeight)
                .coerceAtMost(height.toFloat())
        }

//        if (center) {
//            vTranslate.x = Math.max(vTranslate.x, width - scaleWidth)
//            vTranslate.y = Math.max(vTranslate.y, height - scaleHeight)
//        } else {
//            // TODO: 这里没看懂
//            vTranslate.x = Math.max(vTranslate.x, -scaleWidth)
//            vTranslate.y = Math.max(vTranslate.y, -scaleHeight)
//        }
//
//
//        val maxTx: Float
//        val maxTy: Float
//        if (center) {
//            maxTx = Math.max(0f, (width - scaleWidth) * xPaddingRatio)
//            maxTy = Math.max(0f, (height - scaleHeight) * yPaddingRatio)
//        } else {
//            maxTx = Math.max(0f, width.toFloat())
//            maxTy = Math.max(0f, height.toFloat())
//        }
//6
//        vTranslate.x = Math.min(vTranslate.x, maxTx)
//        vTranslate.y = Math.min(vTranslate.y, maxTy)
    }

    /**
     * 居中偏移
     */
    private fun translateToCenter(scale: Float): PointF {
        satTmp.vTranslate.set(
            width / 2f - (imgWidth / 2f * scale),
            height / 2f - (imgHeight / 2f * scale)
        )
        fitToBounds(true, satTmp)
        return satTmp.vTranslate
    }

    /**
     * 限制scale范围
     */
    private fun limitScale(targetScale: Float): Float {
        var scale = targetScale
        scale = Math.max(minScale(), scale)
        scale = Math.min(maxScale, scale)
        return scale
    }

    private fun minScale(): Float {
        val hPadding = paddingLeft + paddingEnd
        val vPadding = paddingTop + paddingBottom
        return Math.min(
            (width - hPadding) / imgWidth.toFloat(),
            (height - vPadding) / imgHeight.toFloat()
        )
    }

    /**
     * 计算采样率
     */
    private fun calculateInSampleSize(scale: Float): Int {
        var mScale = scale
        if (minTileDpi > 0) {
            val metrics = resources.displayMetrics
            val averageDpi = (metrics.xdpi + metrics.ydpi) / 2
            mScale = minTileDpi / averageDpi * scale
        }

        var inSampleSize = 1

        if (mScale > 0 && mScale < 1) {
            inSampleSize = (1 / mScale).roundToInt()
        }
        var power = 1
        while (power * 2 < inSampleSize) {
            power *= 2
        }
        return power
    }

    /**
     * 画布可绘制Bitmap的最大宽高
     */
    private fun getMaxBitmapDimension(canvas: Canvas): Point {
        return Point(
            canvas.maximumBitmapWidth.coerceAtMost(maxTileWidth),
            canvas.maximumBitmapHeight.coerceAtMost(maxTileHeight)
        )
    }

    private fun createPaints() {
        if (bitmapPaint == null) {
            bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)
                .apply {
                    isFilterBitmap = true
                    //设置图像抖动
                    isDither = true
                }
        }
    }

    private fun onTilesInitialized(
        decoder: ImageRegionDecoder,
        imageWidth: Int,
        imageHeight: Int
    ) {
        this.imgWidth = imageWidth
        this.imgHeight = imageHeight
        imageRegionDecoder = decoder
        requestLayout()
        invalidate()
    }

    private fun onTileLoaded() {
        invalidate()
    }

    // TODO: 改成协程
    /**
     * 初始化decoder，确定图片尺寸
     */
    class TileInitTask(
        bigImageView: BigImageView,
        context: Context,
        decoderFactory: DecoderFactory<out ImageRegionDecoder>,
        private val imageResource: ImageResource
    ) :
        AsyncTask<Void, Void, Point?>() {

        private val viewRef = WeakReference(bigImageView)
        private val contextRef = WeakReference(context)
        private val decoderFactoryRef = WeakReference(decoderFactory)
        private var decoder: ImageRegionDecoder? = null


        override fun doInBackground(vararg p0: Void?): Point? {
            val decoderFactory = decoderFactoryRef.get()
            val view = viewRef.get()
            val context = contextRef.get()
            if (context != null && decoderFactory != null && view != null) {
                decoder = decoderFactory.make()
                return decoder!!.init(context, imageResource.inputStream)
            }
            return null
        }

        override fun onPostExecute(result: Point?) {
            val view = viewRef.get()
            if (view != null) {
                val sDecoder = decoder
                if (result != null && sDecoder != null) {
                    view.onTilesInitialized(sDecoder, result.x, result.y)
                }
            }
        }
    }

    /**
     * 加载切片
     */
    class TileLoadTask(bigImageView: BigImageView, decoder: ImageRegionDecoder, tile: Tile) :
        AsyncTask<Void, Void, Bitmap?>() {

        private val viewRef = WeakReference(bigImageView)
        private val decoderRef = WeakReference(decoder)
        private val tileRef = WeakReference(tile)

        override fun doInBackground(vararg params: Void?): Bitmap? {
            val view = viewRef.get()
            val decoder = decoderRef.get()
            val tile = tileRef.get()
            if (decoder != null && decoder.isReady() && tile != null && tile.visible && view != null) {
                view.decoderLock.readLock().lock()
                try {
                    if (decoder.isReady()) {
                        return decoder.decodeRegion(tile.sRect, tile.sampleSize)
                    } else {
                        tile.loading = false
                    }
                } finally {
                    view.decoderLock.readLock().unlock()
                }
            } else if (tile != null) {
                tile.loading = false
            }
            return null
        }

        override fun onPostExecute(result: Bitmap?) {
            val view = viewRef.get()
            val tile = tileRef.get()
            if (view != null && tile != null) {
                if (result != null) {
                    tile.bitmap = result
                    tile.loading = false
                    view.onTileLoaded()
                }
            }
        }

    }
}