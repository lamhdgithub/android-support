package com.dev.coding.navigate

interface Route : SplashRoute,SettingRoute,MainNavigateRoute {
		companion object : Route by DevRoute()
}

open class DevRoute : Route,
		SplashRoute by SplashRouteImpl(),
		SettingRoute by SettingRouteImpl(),
		MainNavigateRoute by MainNavigateRouteImpl()
