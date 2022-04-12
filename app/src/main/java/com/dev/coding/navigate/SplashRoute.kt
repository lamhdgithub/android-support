package com.dev.coding.navigate

import android.support.core.route.RouteDispatcher
import android.support.core.route.close
import android.support.core.route.open
import com.dev.coding.feature.auth.LoginActivity
import com.dev.coding.feature.main.MainActivity

interface SplashRoute {
		fun RouteDispatcher.redirectToMain()
    fun RouteDispatcher.redirectToLogin()
}

open class SplashRouteImpl : SplashRoute {
		override fun RouteDispatcher.redirectToMain() {
				open<MainActivity>().close()
		}

    override fun RouteDispatcher.redirectToLogin() {
        open<LoginActivity>().close()
    }
}