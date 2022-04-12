package android.support.core.navigation

import android.support.core.di.InjectBy
import android.support.core.di.Injectable
import android.support.core.navigation.v2.FragmentNavigator
import androidx.fragment.app.FragmentManager

@InjectBy(FragmentNavigatorFactoryV2::class)
interface FragmentNavigatorFactory{
    fun create(manager: FragmentManager, containerId: Int): Navigator
}

class FragmentNavigatorFactoryV1 : FragmentNavigatorFactory, Injectable {
    override fun create(manager: FragmentManager, containerId: Int): Navigator {
        return FragmentNavigator(manager, containerId)
    }
}

class FragmentNavigatorFactoryV2 : FragmentNavigatorFactory, Injectable {
    override fun create(manager: FragmentManager, containerId: Int): Navigator {
        return FragmentNavigator(manager, containerId)
    }
}