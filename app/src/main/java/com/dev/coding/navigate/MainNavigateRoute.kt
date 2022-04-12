package com.dev.coding.navigate

import android.support.core.route.RouteDispatcher
import android.support.core.route.open
import com.dev.coding.feature.main.MainNavigationActivity

interface MainNavigateRoute {
		fun openUserDetail(self: RouteDispatcher, email: String)
}

class MainNavigateRouteImpl: MainNavigateRoute{
		override fun openUserDetail(self: RouteDispatcher, email: String) {
				self.open<MainNavigationActivity>(Routing.UserDetail(email))
		}
		
}