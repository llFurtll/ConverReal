package com.convertreal.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.convertreal.fragment.CalculatorFragment
import com.convertreal.fragment.DollarFragment
import com.convertreal.fragment.EuroFragment

class TabAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> {
                DollarFragment()
            }
            1 -> {
                CalculatorFragment()
            }
            else -> {
                EuroFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 3
    }
}