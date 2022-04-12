package android.support.core.navigation

import android.support.core.route.BundleArgument

interface FragmentResultCallback {
    fun onFragmentResult(result: BundleArgument)
}