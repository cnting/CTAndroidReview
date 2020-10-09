package com.cnting.fragmentreview.test_send_msg

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.cnting.fragmentreview.R
import kotlinx.android.synthetic.main.fragment_send_msg2.*

/**
 * Created by cnting on 2020/10/9
 *
 */
class SendMsgFragment2 : Fragment(R.layout.fragment_send_msg2) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendMsgBtn.setOnClickListener {
            val bundle = Bundle().apply { putString("bundleKey", "from SendMsgFragment2") }
            parentFragmentManager.setFragmentResult("key", bundle)
        }
    }
}