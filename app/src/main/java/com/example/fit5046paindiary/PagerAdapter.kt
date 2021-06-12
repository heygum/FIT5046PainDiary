package com.example.fit5046paindiary

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.fit5046paindiary.fragment.BarFragment
import com.example.fit5046paindiary.fragment.LineFragment
import com.example.fit5046paindiary.fragment.PieFragment

class PagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                return PieFragment()
            }
            1 -> {
                return BarFragment()
            }
            2 -> {
                return LineFragment()
            }
            else -> return PieFragment()
        }
    }

    override fun getItemCount(): Int = 3
}