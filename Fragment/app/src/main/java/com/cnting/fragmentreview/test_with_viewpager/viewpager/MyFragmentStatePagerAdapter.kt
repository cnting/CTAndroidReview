package com.cnting.fragmentreview.test_with_viewpager.viewpager

import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.cnting.fragmentreview.test_with_viewpager.MyFragment

/**
 * Created by cnting on 2020/9/28
 *
 */
class MyFragmentStatePagerAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(
        fragmentManager
//    , BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT   //这里会影响前后fragment的生命周期
    ) {

    override fun getItem(position: Int): Fragment {
        return MyFragment()
            .apply { arguments = Bundle().apply { putInt("position", position) } }
    }

    override fun getCount(): Int {
        return 10
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)

    }
}