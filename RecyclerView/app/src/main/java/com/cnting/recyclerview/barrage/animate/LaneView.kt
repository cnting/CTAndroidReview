package com.cnting.recyclerview.barrage.animate

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Rect
import android.util.ArrayMap
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.View.OnLayoutChangeListener
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import com.cnting.recyclerview.barrage.animate.pool.SimplePool
import java.util.*

/**
 * Created by cnting on 2022/12/16
 * https://juejin.cn/post/7004603099113340936
 * 弹幕-使用动画实现
 */
class LaneView : ViewGroup {
    constructor(context: Context) : super(context)
    constructor(
        context: Context,
        attrs: AttributeSet
    ) : super(context, attrs)

    constructor (
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    private lateinit var pool: SimplePool<View>
    var poolCapacity: Int = 20
        set(value) {
            field = value
            pool = SimplePool(value)
        }
    lateinit var createView: () -> View
    lateinit var bindView: (Any, View) -> Unit
    private var verticalGap = 10
    private var horizontalGap = 10
    private var datas = emptyList<Any>()

    //记录所有泳道
    private var laneMap = mutableMapOf<Int, Lane>()
    var onItemClick: ((View, Any) -> Unit)? = null

    init {
        poolCapacity = 20
    }

    private fun obtain(): View = pool.acquire() ?: createView()

    private fun recycle(view: View) {
        view.detach()
        pool.release(view)
    }

    private fun show(data: Any) {
        post {
            //1.生成新的子控件
            val child: View = obtain()
            //2.为子控件绑定数据
            bindView(data, child)
            //3.测量子控件
            val width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            val height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            child.measure(width, height)
            //4.将子控件添加到容器控件
            addView(child)
            //5.布局子控件
            val left = measuredWidth  //子控件的左侧位于弹幕控件的右侧
            val top = getRandomTop(child.measuredHeight)
            child.layout(left, top, left + child.measuredWidth, top + child.measuredHeight)
            //6.开启子控件动画
            laneMap[top]?.add(child, data) ?: run {
                Lane(measuredWidth).also {
                    it.add(child, data)
                    laneMap[top] = it
                    it.showNext()
                }
            }

        }
    }

    fun show(datas: List<Any>) {
        this.datas = datas
        datas.forEach { show(it) }
    }

    fun pause() {
        laneMap.values.forEach { lane ->
            lane.pause()
        }
    }

    fun resume() {
        laneMap.values.forEach { lane ->
            lane.resume()
        }
    }

    fun saveSnapshot(): List<ViewPosition> {
        val viewPositions = mutableListOf<List<ViewPosition>>()
        laneMap.values.forEach { lane ->
            viewPositions.add(lane.snapshot())
        }
        return viewPositions.flatten()
    }

    fun restoreSnapshot(viewPositions: List<ViewPosition>) {
        viewPositions.forEach { position ->
            val child = obtain()
            addView(child)
            measureChild(
                child,
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            )
            val left = position.left
            val top = position.top
            child.layout(left, top, left + child.measuredWidth, top + child.measuredHeight)
        }
    }

    private fun getRandomTop(commentHeight: Int): Int {
        //计算布局泳道的可用高度
        val lanesHeight = measuredHeight - paddingTop - paddingBottom
        //计算可用高度中最多能布局几条泳道
        val lanesCapacity = (lanesHeight + verticalGap) / (commentHeight + verticalGap)
        //计算可用高度布局完所有泳道后剩余空间
        val extraPadding =
            lanesHeight - commentHeight * lanesCapacity - verticalGap * (lanesCapacity - 1)
        //计算第一条泳道相对于容器控件的mTop值
        val firstLaneTop = paddingTop + extraPadding / 2
        val randomOffset = (0 until lanesCapacity).random() * (commentHeight + verticalGap)
        return firstLaneTop + randomOffset
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    }

    private val gestureDetector =
        GestureDetector(context, object : GestureDetector.OnGestureListener {
            override fun onDown(e: MotionEvent?): Boolean {
                return false
            }

            override fun onShowPress(e: MotionEvent?) {
            }

            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                e?.let {
                    findDataUnder(it.x, it.y)?.let { pair ->
                        onItemClick?.invoke(pair.first, pair.second)
                    }
                }
                return false
            }

            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent?,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                return false
            }

            override fun onLongPress(e: MotionEvent?) {
            }

            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent?,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                return false
            }
        })


    private fun findDataUnder(x: Float, y: Float): Pair<View, Any>? {
        var pair: Pair<View, Any>? = null
        laneMap.values.forEach { lane ->
            lane.forEachView { view, data ->
                view.getRelativeRectTo(this@LaneView).also { rect ->
                    if (rect.contains(x.toInt(), y.toInt())) {
                        pair = view to data
                    }
                }
            }
        }
        return pair
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        gestureDetector.onTouchEvent(ev)
        return super.dispatchTouchEvent(ev)
    }

    //泳道
    inner class Lane(var laneWidth: Int) {
        //弹幕视图队列
        private var viewQueue = LinkedList<View>()
        private val dataQueue = LinkedList<Any>()
        private var currentView: View? = null
        private val viewDataMap = ArrayMap<View, ViewData>()

        //用于限制泳道内弹幕间距
        private var blockShow = false
        private val onLayoutChangeListener =
            OnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                //只有当前一个弹幕滚动的足够远，才开启下一个弹幕的动画
                if (laneWidth - left > v.measuredWidth + horizontalGap) {
                    blockShow = false
                    showNext()
                }
            }

        //开始该泳道中下一个弹幕的滚动
        fun showNext() {
            //还未到展示下一个弹幕，直接返回
            if (blockShow) return
            currentView?.removeOnLayoutChangeListener(onLayoutChangeListener)
            //从泳道队列中取出弹幕视图
            currentView = viewQueue.poll()
            currentView?.let { view ->
                view.addOnLayoutChangeListener(onLayoutChangeListener)
                //计算每个弹幕的动画时间
                val distance = laneWidth + view.measuredWidth
                val speed = laneWidth.toFloat() / 4000
                val duration = (distance / speed).toLong()
                val valueAnimator = ValueAnimator.ofFloat(1f).apply {
                    setDuration(duration)
                    interpolator = LinearInterpolator()
                    addUpdateListener {
                        val value = it.animatedFraction
                        val left = (laneWidth - value * (laneWidth + view.measuredWidth)).toInt()
                        //通过重新布局来实现弹幕视图的滚动
                        view.layout(
                            left,
                            view.top,
                            left + view.measuredWidth,
                            view.top + view.measuredHeight
                        )
                    }
                    addListener(object : AnimatorListener {
                        override fun onAnimationStart(animation: Animator?) {
                        }

                        override fun onAnimationEnd(animation: Animator?) {
                            recycle(view)
                            viewDataMap.remove(view)
                        }

                        override fun onAnimationCancel(animation: Animator?) {
                        }

                        override fun onAnimationRepeat(animation: Animator?) {
                        }
                    })
                }
                valueAnimator.start()
                dataQueue.poll()?.let { viewDataMap[view] = ViewData(it, valueAnimator) }
                blockShow = true
            }
        }

        fun add(view: View, data: Any) {
            viewQueue.addLast(view)
            dataQueue.addLast(data)
            showNext()
        }


        fun forEachView(each: (View, Any) -> Any) {
            viewDataMap.forEach { entry -> each(entry.key, entry.value.data) }
        }

        fun pause() {
            viewDataMap.values.forEach { viewData ->
                viewData.animator.pause()
            }
        }

        fun resume() {
            viewDataMap.values.forEach { viewData ->
                viewData.animator.resume()
            }
        }

        fun snapshot(): List<ViewPosition> {
            val viewPositions = mutableListOf<ViewPosition>()
            viewDataMap.forEach { entry ->
                viewPositions.add(ViewPosition(entry.key.left, entry.key.top, entry.value.data))
            }
            return viewPositions
        }

        inner class ViewData(var data: Any, var animator: ValueAnimator)
    }


}

private fun View?.detach() = this?.parent?.let { it as? ViewGroup }?.also { it.removeView(this) }

fun View.getRelativeRectTo(otherView: View): Rect {
    //将子视图和父视图置于同一个全局坐标系，并获取他们的矩形区域
    val parentRect = Rect().also { otherView.getLocalVisibleRect(it) }
    val childRect = Rect().also { getGlobalVisibleRect(it) }
    //获取父子视图矩形区域的相对位置
    return childRect.relativeTo(parentRect)
}

fun Rect.relativeTo(otherRect: Rect): Rect {
    val relativeLeft = left - otherRect.left
    val relativeTop = top - otherRect.top
    val relativeRight = relativeLeft + right - left
    val relativeBottom = relativeTop + bottom - top
    return Rect(relativeLeft, relativeTop, relativeRight, relativeBottom)
}

class ViewPosition(var left: Int, var top: Int, var data: Any)

data class Comment(val text: String, val url: String)