package com.cnting.fragmentreview.test_fragment_manager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.cnting.fragmentreview.R

/**
 * Created by cnting on 2020/9/27
 *
 */
class ParentFragment : Fragment(R.layout.fragment_test_lifecycle) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.commit { add(R.id.parentFragmentContainer, ChildFragment()) }

        Log.d("ParentFragment", "===>parentFragmentManager:$parentFragmentManager")
        Log.d("ParentFragment", "===>childFragmentManager:$childFragmentManager")
    }
}

class ChildFragment : Fragment(R.layout.fragment_test_lifecycle2) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ChildFragment", "===>parentFragmentManager:$parentFragmentManager")
        Log.d("ChildFragment", "===>childFragmentManager:$childFragmentManager")
    }
}