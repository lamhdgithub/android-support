package android.support.core.navigation

interface Backable {
    fun onInterceptBackPress(): Boolean
}