package com.cnting.fragmentreview.test_send_msg

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import com.cnting.fragmentreview.R
import kotlinx.android.synthetic.main.fragment_send_msg1.*

/**
 * Created by cnting on 2020/10/9
 *
 */
class SendMsgFragment1 : Fragment(R.layout.fragment_send_msg1) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentFragmentManager.setFragmentResultListener(
            "key",
            this,
            FragmentResultListener { requestKey, result ->
                receiveMsgTv.text = result.getString("bundleKey")
            })
    }
}