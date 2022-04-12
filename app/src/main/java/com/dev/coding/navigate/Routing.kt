package com.dev.coding.navigate

import android.support.core.navigation.NavOptions
import android.support.core.navigation.Navigator
import android.support.core.route.BundleArgument
import androidx.fragment.app.Fragment
import com.dev.coding.R
import com.dev.coding.feature.list.UserDetailFragment
import com.dev.coding.feature.list.UserFragment
import kotlinx.parcelize.Parcelize
import kotlin.reflect.KClass


interface IRoute : BundleArgument {
    val fragmentClass: KClass<out Fragment>
}

class Routing {
    @Parcelize
    class UserDetail(val email: String) : IRoute {
        override val fragmentClass: KClass<out Fragment>
            get() = UserDetailFragment::class
    }
}

fun animOptions() = NavOptions(
    animEnter = R.anim.slide_in_from_right,
    animExit = R.anim.slide_out_to_left,
    animPopEnter = R.anim.slide_in_from_left,
    animPopExit = R.anim.slide_out_to_right
)