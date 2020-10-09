package com.cnting.fragmentreview.test_send_msg

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.cnting.fragmentreview.R
import kotlinx.android.synthetic.main.activity_test_send_msg.*

/**
 * Created by cnting on 2020/10/9
 *
 */
class TestSendMsgActivity : AppCompatActivity(R.layout.activity_test_send_msg) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.commit {
            add(R.id.sendMsgFragmentContainer1, SendMsgFragment1())
            add(R.id.sendMsgFragmentContainer2, SendMsgFragment2())
        }
    }
}