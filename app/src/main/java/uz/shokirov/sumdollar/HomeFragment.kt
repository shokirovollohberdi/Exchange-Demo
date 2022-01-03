package uz.shokirov.sumdollar

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.item_tab.view.*
import uz.shokirov.adapter.MyFragmentAdapter
import uz.shokirov.sumdollar.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var adapter: MyFragmentAdapter

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(LayoutInflater.from(context))

        adapter = MyFragmentAdapter(fragmentManager!!)
        binding.viewpager.adapter = adapter
        binding.tablayout.setupWithViewPager(binding.viewpager)


        setTab()

        return binding.root
    }

    private fun setTab() {
        val tapCount = binding.tablayout.tabCount
        var tabList = arrayListOf<Int>(R.drawable.ic_baseline_monetization_on_24,R.drawable.ic_baseline_info_24)

        for (i in 0 until tapCount) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_tab, null, false)
            var tab = binding.tablayout.getTabAt(i)
            tab?.customView = view
            view.iconTab.setImageResource(tabList[i])
        }
    }
}