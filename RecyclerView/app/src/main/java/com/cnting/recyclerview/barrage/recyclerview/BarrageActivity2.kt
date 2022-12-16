package com.cnting.recyclerview.barrage.recyclerview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.cnting.recyclerview.R
import com.cnting.recyclerview.barrage.animate.Comment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

/**
 * Created by cnting on 2022/12/16
 * 弹幕
 */
class BarrageActivity2 : AppCompatActivity() {

    val pic = "https://p.qqan.com/up/2021-4/16189694769878569.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barrage2)
        val recyclerView = findViewById<RecyclerView>(R.id.laneView)
        recyclerView.layoutManager = LaneLayoutManager()
        recyclerView.adapter = LaneAdapter(
            listOf(
                Comment("1111111", pic),
                Comment("22222222", pic),
                Comment("dsljilfsijlxi", pic),
                Comment("受理费巨力索具李经理四", pic),
                Comment("发病机理忘记了佛饿哦i；我", pic),
                Comment("列举了斯嘉丽吉里吉里", pic),
                Comment("巴黎世家浪费IE肌无力进啦", pic),
                Comment("大咧咧斯嘉丽就类似", pic),
                Comment("布鲁斯家乐福i金额", pic),
                Comment("额外列家里现金流", pic),
                Comment("巴黎世家饿了费死劲福利姬", pic),
                Comment("不理人吉里吉里", pic),
                Comment("额外类五级哦我i", pic),
                Comment("别的工地生日歌", pic),
                Comment("收费额发生过个人", pic),
                Comment("离地距离吉里吉里", pic),
                Comment("大哥分手发", pic),
                Comment("练习卷理解理解", pic),
                Comment("的发热丝", pic),
                Comment("大概和噶德国", pic),
                Comment("我饿精力不济李经理", pic),
                Comment("分公司答复", pic),
                Comment("白兰地节日里", pic),
                Comment("水利局不理解独立日", pic),
                Comment("饿了s诶救了我i哦i就立卡酒吧里", pic),
                Comment("不讲理吉里吉里数据额里手机里", pic),
                Comment("色李经理手机里", pic),
                Comment("22222222", pic),
                Comment("效力纠纷理解理解", pic),
                Comment("鹅历险记管理记录", pic),
                Comment("百里杜鹃管理经理", pic),
                Comment("不理解必利劲两次奖励", pic),
                Comment("额理性攻击力任经理", pic),
                Comment("22222222", pic),
                Comment("释放了司机联系高渐离就来上班李", pic),
                Comment("份额里几个李经理", pic),
                Comment("22222222", pic),
                Comment("额三类经历过i就", pic),
                Comment("俄罗斯激励理论经理说", pic),
                Comment("22222222", pic),
                Comment("两个i办理军绿色栗色类似经历", pic)
            )
        )

        countdown(Long.MAX_VALUE, 500) {
            recyclerView.smoothScrollBy(10, 0)
        }.launchIn(MainScope())
    }

    private fun <T> countdown(
        duration: Long,
        interval: Long,
        onCountDown: suspend (Long) -> T
    ): Flow<T> =
        flow { (duration - interval downTo 0 step interval).forEach { emit(it) } }
            .onEach { delay(interval) }
            .onStart { emit(duration) }
            .map(onCountDown)
            .flowOn(Dispatchers.Default)
}