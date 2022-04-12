package com.dev.coding.navigate

import android.support.core.route.RouteDispatcher
import android.support.core.route.close
import android.support.core.route.open
import com.dev.coding.feature.auth.LoginActivity

interface SettingRoute {
    fun RouteDispatcher.closeAndOpenLogin()
}

class SettingRouteImpl : SettingRoute {
    override fun RouteDispatcher.closeAndOpenLogin() {
        open<LoginActivity>().close()
    }
}