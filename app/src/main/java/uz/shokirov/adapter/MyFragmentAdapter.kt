package uz.shokirov.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import uz.shokirov.sumdollar.SettingsFragment
import uz.shokirov.sumdollar.ValyutaFragment

class MyFragmentAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(
    fragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                return ValyutaFragment()
            }
            1 -> {
                return SettingsFragment()
            }
            else -> null!!
        }

    }
}