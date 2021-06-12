package com.example.fit5046paindiary.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.fit5046paindiary.PagerAdapter
import com.example.fit5046paindiary.R
import com.example.fit5046paindiary.databinding.ReportFragmentBinding
import com.google.android.material.tabs.TabLayoutMediator

class ReportFragment:Fragment() {
    private var _binding: ReportFragmentBinding? = null
    private val binding get()  = _binding!!
    private lateinit var pagerAdapter: PagerAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ReportFragmentBinding.inflate(inflater,container,false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pagerAdapter = PagerAdapter(this)
        viewPager = binding.pager
        viewPager.adapter = pagerAdapter
        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Pie Chart"
                    tab.icon = context?.getDrawable(R.drawable.outline_pie_chart_black_36)
                }
                1 -> {
                    tab.text = "Bar Chart"
                    tab.icon = context?.getDrawable(R.drawable.outline_bar_chart_black_36)
                }
                2 -> {
                    tab.text = "Line Chart"
                    tab.icon = context?.getDrawable(R.drawable.outline_show_chart_black_36)
                }
            }
        }.attach()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}