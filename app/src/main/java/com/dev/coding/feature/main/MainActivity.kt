package com.dev.coding.feature.main

import android.os.Bundle
import android.support.core.navigation.NavOptions
import android.support.core.navigation.Navigator
import android.support.core.navigation.findNavigator
import android.support.core.view.viewBinding
import androidx.fragment.app.Fragment
import com.dev.coding.R
import com.dev.coding.base.BaseActivity
import com.dev.coding.databinding.ActivityMainBinding
import com.dev.coding.feature.list.UserFragment
import com.dev.coding.feature.setting.SettingFragment
import com.dev.coding.feature.tab.HelpFragment
import com.dev.coding.helper.pairLookupOf
import com.dev.coding.views.widget.topbar.TopBarAdapter
import com.dev.coding.views.widget.topbar.TopBarAdapterImpl
import com.dev.coding.views.widget.topbar.TopBarOwner
import kotlin.reflect.KClass

class MainActivity : BaseActivity(R.layout.activity_main), TopBarOwner {
    private val binding by viewBinding(ActivityMainBinding::bind)
    override lateinit var topBar: TopBarAdapter
    
    @Suppress("unchecked_cast")
    val route = pairLookupOf(
        R.id.mnUser to UserFragment::class as KClass<Fragment>,
        R.id.mnHelp to HelpFragment::class,
        R.id.mnSetting to SettingFragment::class
    )
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        topBar = TopBarAdapterImpl(this, binding.topBar)
        val navigator = findNavigator()
        navigator.addDestinationChangeListener {
            binding.navigationBar.selectedItemId = route.requireKey(it)
        }
        binding.navigationBar.setOnItemSelectedListener {
            navigator.navigateTo(it.itemId)
            true
        }
        if (savedInstanceState == null) navigator.navigateTo(R.id.mnUser)
    }

    override fun onBackPressed() {
        if (!findNavigator().navigateUp()) super.onBackPressed()
    }
    
    private fun Navigator.navigateTo(id: Int): Boolean {
        val des = route.requireValue(id)
        val shouldNavigate = lastDestination?.kClass != des
        if (shouldNavigate) navigate(
            des, navOptions = NavOptions(
                popupTo = UserFragment::class,
                reuseInstance = true,
                inclusive = true
            )
        )
        return shouldNavigate
    }
}