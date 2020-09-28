package com.cnting.fragmentreview.test_with_viewpager.viewpager

import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.cnting.fragmentreview.test_with_viewpager.MyFragment

/**
 * Created by cnting on 2020/9/28
 *
 */
class MyFragmentPagerAdapter(fragmentManager: FragmentManager) :
    FragmentPagerAdapter(
        fragmentManager
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