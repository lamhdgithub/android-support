package com.dev.coding.feature.tab

import android.os.Bundle
import android.support.core.view.viewBinding
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.dev.coding.DataConst.Tab
import com.dev.coding.R
import com.dev.coding.base.BaseFragment
import com.dev.coding.databinding.FragmentHelpBinding
import com.dev.coding.views.widget.ViewPager2Adapter
import com.dev.coding.views.widget.topbar.TextCenterTopBarState
import com.dev.coding.views.widget.topbar.TopBarOwner
import com.google.android.material.tabs.TabLayout

class HelpFragment : BaseFragment(R.layout.fragment_help),TopBarOwner {
		private val binding by viewBinding(FragmentHelpBinding::bind)
		
		override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
				super.onViewCreated(view, savedInstanceState)
				topBar.setState(TextCenterTopBarState(R.string.title_help))
				Adapter(binding.viewPager, this).apply {
						submit(Tab.help)
						setupPageChangeWith(binding.tabLayout)
				}
		}
		
		class Adapter(viewPager: ViewPager2, private val fragment: Fragment) :
				ViewPager2Adapter(viewPager, fragment) {
				
				override fun getItemCount(): Int {
						return mRoute?.size ?: 0
				}
				
				override fun createFragment(position: Int): Fragment {
						return mRoute?.get(position)?.second ?: HelpTab1Fragment()
				}
				
				override fun onBindTab(position: Int, tab: TabLayout.Tab) {
						tab.text = fragment.getText(mRoute?.get(position)?.first ?: 0)
				}
		}
}