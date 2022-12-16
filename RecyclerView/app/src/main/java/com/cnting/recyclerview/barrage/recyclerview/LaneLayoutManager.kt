package com.cnting.recyclerview.barrage.recyclerview

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

/**
 * Created by cnting on 2022/12/16
 * https://juejin.cn/post/7010521583894659103
 * 自定义LayoutManager实现弹幕
 */
class LaneLayoutManager : RecyclerView.LayoutManager() {
    //标记填充结束
    private val LAYOUT_FINISH = -1

    //列表适配器索引
    private var adapterIndex = 0

    //初次填充过程中的上一个被填充的弹幕
    private var lastLaneEndView: View? = null

    //所有泳道
    private var lanes = mutableListOf<Lane>()

    var verticalGap = 35
    var horizontalGap = 20

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        fillLanes(recycler)
    }

    //创建泳道用填充每个泳道的第一个弹幕
    private fun fillLanes(recycler: RecyclerView.Recycler?) {
        lastLaneEndView = null
        //如果列表垂直方向上还有空间则继续填充弹幕
        while (hasMoreLane(height - lanes.bottom())) {
            //填充单个弹幕到泳道中
            val consumeSpace = layoutView(recycler)
            if (consumeSpace == LAYOUT_FINISH) break
        }
    }

    //填充单个表项
    private fun layoutView(recycler: RecyclerView.Recycler?): Int {
        //从缓冲池中获取表项视图
        val view = recycler?.getViewForPosition(adapterIndex) ?: return LAYOUT_FINISH
        measureChildWithMargins(view,0,0)
        val verticalMargin =
            (view.layoutParams as RecyclerView.LayoutParams).let { it.topMargin + it.bottomMargin }
        val consumed =
            getDecoratedMeasuredHeight(view) + if (lastLaneEndView == null) 0 else verticalGap + verticalMargin
        //若列表垂直方向还可以容纳一条新泳道，则新建泳道，否则停止填充
        if (height - lanes.bottom() - consumed > 0) {
            lanes.add(emptyLane(adapterIndex))
        } else return LAYOUT_FINISH

        //添加
        addView(view)

        //获取最新追加的泳道
        val lane = lanes.last()
        //计算当前表项上下左右边框
        val left = lane.end + horizontalGap
        val top =
            if (lastLaneEndView == null) paddingTop else lastLaneEndView!!.bottom + verticalGap
        val right = left + view.measuredWidth
        val bottom = top + view.measuredHeight
        //布局
        layoutDecorated(view, left, top, right, bottom)
        //更新泳道末尾横坐标及布局索引
        lane.apply {
            end = right
            endLayoutIndex = childCount - 1
        }

        adapterIndex++
        lastLaneEndView = view
        return consumed
    }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        return scrollBy(dx, recycler)
    }

    override fun canScrollHorizontally(): Boolean {
        return true
    }

    private fun scrollBy(dx: Int, recycler: RecyclerView.Recycler?): Int {
        if (childCount == 0 || dx == 0) return 0
        //在滚动还未开始前，更新泳道信息
        updateLanesEnd()
        //获取滚动绝对值
        val absDx = abs(dx)
        //遍历所有泳道，向其中的枯竭泳道填充弹幕
        lanes.forEach { lane ->
            if (lane.isDrainOut(absDx)) layoutViewByScroll(recycler, lane)
        }
        //回收弹幕
        recycleGoneView(absDx, recycler)
        //滚动列表的落脚点：将表项向手指位移的反方向平移相同距离
        offsetChildrenHorizontal(-absDx)
        return dx
    }

    //回收弹幕
    private fun recycleGoneView(dx: Int, recycler: RecyclerView.Recycler?) {
        recycler ?: return
        lanes.forEach { lane ->
            //获取泳道头部弹幕
            getChildAt(lane.startLayoutIndex)?.let { startView ->
                //如果泳道头部弹幕已经滚出屏幕则回收它
                if (isGoneByScroll(startView, dx)) {
                    //回收弹幕视图
                    removeAndRecycleView(startView, recycler)
                    //更新泳道信息
                    updateLaneIndexAfterRecycle(lane.startLayoutIndex)
                    lane.startLayoutIndex += lanes.size - 1
                }

            }
        }
    }

    private fun updateLaneIndexAfterRecycle(recycleIndex: Int) {
        lanes.forEach { lane ->
            if (lane.startLayoutIndex > recycleIndex) {
                lane.startLayoutIndex--
            }
            if (lane.endLayoutIndex > recycleIndex) {
                lane.endLayoutIndex--
            }
        }
    }


    //更新泳道信息
    private fun updateLanesEnd() {
        lanes.forEach { lane ->
            lane.getEndView()?.let {
                lane.end = getEnd(it)
            }
        }
    }

    //弹幕滚动时填充新弹幕
    private fun layoutViewByScroll(recycler: RecyclerView.Recycler?, lane: Lane) {
        val view = recycler?.getViewForPosition(adapterIndex) ?: return
        measureChildWithMargins(view, 0, 0)
        addView(view)

        val left = lane.end + horizontalGap
        val top = lane.getEndView()?.top ?: paddingTop
        val right = left + view.measuredWidth
        val bottom = top + view.measuredHeight
        layoutDecorated(view, left, top, right, bottom)
        lane.apply {
            end = right
            endLayoutIndex = childCount - 1
        }
        adapterIndex++
    }

    data class Lane(
        var end: Int,  //泳道末尾弹幕横坐标（最后一个弹幕的right值）
        var endLayoutIndex: Int,  //泳道末尾弹幕的布局索引
        var startLayoutIndex: Int  //泳道头部弹幕的布局索引
    )

    private fun List<Lane>.bottom() = lastOrNull()?.getEndView()?.bottom ?: 0

    private fun Lane.getEndView(): View? = getChildAt(endLayoutIndex)

    //泳道是否枯竭（最后一个弹幕的right向左平移dx后是否小于列表宽度，若小于则表示泳道中的弹幕都展示完了，要继续填充）
    private fun Lane.isDrainOut(dx: Int): Boolean = getEnd(getEndView()) - dx < width

    //弹幕是否滚出屏幕
    private fun isGoneByScroll(view: View, dx: Int): Boolean = getEnd(view) - dx < 0

    //获取表项的right值
    private fun getEnd(view: View?) =
        if (view == null) Int.MIN_VALUE
        else getDecoratedRight(view) + (view.layoutParams as RecyclerView.LayoutParams).rightMargin

    private fun emptyLane(adapterIndex: Int) =
        Lane(-horizontalGap + width, -1, getLayoutIndex(adapterIndex))

    private fun getLayoutIndex(adapterIndex: Int): Int {
        val firstChildIndex =
            getChildAt(0)?.let { (it.layoutParams as RecyclerView.LayoutParams).bindingAdapterPosition }
                ?: 0
        return adapterIndex - firstChildIndex
    }

    private fun hasMoreLane(remainSpace: Int) = remainSpace > 0 && adapterIndex in 0 until itemCount
}