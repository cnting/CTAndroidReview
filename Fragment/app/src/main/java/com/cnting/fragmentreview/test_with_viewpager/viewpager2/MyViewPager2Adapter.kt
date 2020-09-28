package com.cnting.fragmentreview.test_with_viewpager.viewpager2

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cnting.fragmentreview.test_with_viewpager.MyFragment

/**
 * Created by cnting on 2020/9/28
 *
 */
class MyViewPager2Adapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {


    override fun getItemCount(): Int {
        return 10;
    }

    override fun createFragment(position: Int): Fragment {
        return MyFragment()
            .apply {
                arguments = Bundle().apply {
                    putInt("position", position)
                    putBoolean("isViewPager2", true)
                }
            }
    }

}