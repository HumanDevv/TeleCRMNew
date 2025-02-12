package com.tele.crm.presentation.addleads

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tele.crm.presentation.addleads.tabs.DoubleLeadFragment
import com.tele.crm.presentation.addleads.tabs.SingleLeadFragment

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SingleLeadFragment()
            1 -> DoubleLeadFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
