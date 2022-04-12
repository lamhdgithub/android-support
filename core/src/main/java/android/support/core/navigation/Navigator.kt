package android.support.core.navigation

import android.os.Bundle
import android.support.core.route.BundleArgument
import android.support.core.SavedStateCallback
import android.support.core.registry
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.lifecycle
import androidx.fragment.app.savedStateRegistry
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.savedstate.SavedStateRegistry
import java.util.*
import kotlin.reflect.KClass

abstract class Navigator(
    protected val fragmentManager: FragmentManager,
    @IdRes val container: Int
) {
    companion object {
        private const val KEY_SAVED_STATE = "com:support:core:navigation:navigator"
    }

    abstract val lastDestination: Destination?
    private val mTransactionManager = TransactionManager()
    private var mExecutable: Boolean = true
    private var mDestinationChangeListeners = arrayListOf<OnDestinationChangeListener>()
    private val mTagManager = FragmentTagManager()
    protected val tagManager get() = mTagManager

    protected val Destination.requireFragment: Fragment
        get() = fragment ?: error("Not found requireFragment $tag")

    protected val Destination.fragment: Fragment?
        get() = fragmentManager.findFragmentByTag(tag)

    private val mSavedStateListener = object : SavedStateCallback {
        override fun onSavedState(): Bundle = Bundle().apply(::onSaveInstance)

        override fun onRestoreState(savedState: Bundle) = onRestoreInstance(savedState)
    }

    // Fix for case "Can not perform this action after onSaveInstanceState"
    private val mObserver = object : LifecycleEventObserver {
        private var mSavedStateRegistry: SavedStateRegistry? = null

        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    mSavedStateRegistry = fragmentManager.savedStateRegistry
                    mSavedStateRegistry!!.registry(KEY_SAVED_STATE, mSavedStateListener)
                }
                Lifecycle.Event.ON_DESTROY -> {
                    source.lifecycle.removeObserver(this)
                    mSavedStateRegistry?.unregisterSavedStateProvider(KEY_SAVED_STATE)
                    mDestinationChangeListeners.clear()
                    mSavedStateRegistry = null
                }
                Lifecycle.Event.ON_START -> {
                    if (!mExecutable) {
                        mExecutable = true
                        mTransactionManager.executeIfNeeded()
                    }
                }
                else -> {
                }
            }
        }
    }

    init {
        fragmentManager.lifecycle.addObserver(mObserver)
    }

    protected open fun notifyDestinationChange(kClass: KClass<out Fragment>) {
        mDestinationChangeListeners.forEach { it.onDestinationChanged(kClass) }
    }

    fun addDestinationChangeListener(function: OnDestinationChangeListener) {
        if (mDestinationChangeListeners.contains(function)) return
        mDestinationChangeListeners.add(function)
    }

    fun removeDestinationChangeListener(function: OnDestinationChangeListener) {
        mDestinationChangeListeners.remove(function)
    }

    abstract fun navigate(
        kClass: KClass<out Fragment>,
        args: Bundle? = null,
        navOptions: NavOptions? = null
    )

    fun navigateUp(): Boolean {
        return navigateUp(null)
    }

    abstract fun navigateUp(result: BundleArgument?): Boolean

    open fun popBackStack(popupTo: KClass<out Fragment>, inclusive: Boolean): Boolean {
        throw UnsupportedOperationException("Not support, please use FragmentNavigator version 2")
    }

    @CallSuper
    protected open fun onSaveInstance(state: Bundle) {
        mExecutable = false
        mTagManager.saveState(state)
    }

    protected open fun onRestoreInstance(saved: Bundle) {
        mTagManager.restoreState(saved)
    }

    protected fun transaction(function: SupportFragmentTransaction. () -> Unit) {
        mTransactionManager.push(Transaction(function))
    }

    private inner class TransactionManager {
        private val mTransactions = ArrayDeque<Transaction>()
        private val isEmpty get() = mTransactions.isEmpty()
        private val next get() = mTransactions.takeIf { it.isNotEmpty() }?.peekFirst()

        fun push(transaction: Transaction) = synchronized(this) {
            transaction.onFinishListener = {
                mTransactions.pop()
                next?.execute()
            }

            val shouldExecute = isEmpty && mExecutable
            mTransactions.add(transaction)
            if (shouldExecute) transaction.execute()
        }

        fun executeIfNeeded() {
            if (mTransactions.isEmpty()) return
            mTransactions.first.execute()
        }
    }

    private inner class Transaction(private val function: SupportFragmentTransaction.() -> Unit) {
        var onFinishListener: (() -> Unit)? = null

        fun execute() {
            fragmentManager.beginTransaction().also {
                val tran = SupportFragmentTransactionImpl(it)
                tran.function()
                it.runOnCommit { onFinishListener?.invoke() }
                it.commit()
            }
        }
    }


}