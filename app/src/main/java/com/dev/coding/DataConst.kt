package com.dev.coding

import com.dev.coding.feature.tab.HelpTab1Fragment
import com.dev.coding.feature.tab.HelpTab2Fragment

object DataConst {
		object Tab {
				val help = listOf(
						R.string.title_tab_1 to HelpTab1Fragment(),
						R.string.title_tab_2 to HelpTab2Fragment()
				)
		}
}