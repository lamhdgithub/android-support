package com.dev.coding.feature.main

import android.os.Bundle
import android.support.core.navigation.findNavigator
import android.support.core.navigation.navigate
import android.support.core.route.BundleArgument
import android.support.core.route.argument
import com.dev.coding.R
import com.dev.coding.base.BaseActivity
import com.dev.coding.feature.list.UserDetailFragment
import com.dev.coding.navigate.Routing
import com.dev.coding.views.widget.topbar.TopBarAdapter
import com.dev.coding.views.widget.topbar.TopBarAdapterImpl
import com.dev.coding.views.widget.topbar.TopBarOwner


class MainNavigationActivity : BaseActivity(R.layout.fragment_main_navigation), TopBarOwner {
    override lateinit var topBar: TopBarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        topBar = TopBarAdapterImpl(this, findViewById(R.id.topBar))
        val navigator = findNavigator()
        val args = argument<BundleArgument>()

        if (savedInstanceState == null) {
            val clazz = when (args) {
                is Routing.UserDetail -> UserDetailFragment::class
                else -> error("Not support")
            }
            navigator.navigate(clazz, args)
        }
    }

    override fun onBackPressed() {
        if (!findNavigator().navigateUp()) super.onBackPressed()
    }
}