package android.support.core.viewmodel

import android.support.core.R
import android.support.core.event.ErrorEvent
import android.support.core.event.LoadingEvent
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


interface ViewModelRegistrable {
    @CallSuper
    fun registry(viewModel: ViewModel) {
        val tagId = R.id.tag_registry_view_model

        val callback: (View) -> Unit = { container ->
            var register = container.getTag(tagId) as? ViewModelRegister
            if (register == null) {
                register = ViewModelRegister()
                container.setTag(tagId, register)
            }

            val isViewModelRegistered = register.isRegistered(viewModel.javaClass.name)
            if (!isViewModelRegistered) {
                onRegistryViewModel(viewModel)
                onReRegistryViewModel(viewModel)
                register.save(viewModel.javaClass.name)
            } else {
                onReRegistryViewModel(viewModel)
            }
        }
        when (this) {
            is FragmentActivity -> ViewModelRegister.ofActivity(this, callback)
            is Fragment -> ViewModelRegister.ofFragment(this, callback)
            else -> error("${this.javaClass.name} should be Activity or Fragment")
        }
    }

    fun onRegistryViewModel(viewModel: ViewModel)
    fun onReRegistryViewModel(viewModel: ViewModel) {}

    private class ViewModelRegister {
        private val mCached = hashMapOf<String, Boolean>()

        fun isRegistered(name: String): Boolean {
            return mCached.containsKey(name)
        }

        fun save(name: String) {
            mCached[name] = true
        }

        companion object {
            fun ofActivity(
                activity: FragmentActivity,
                callback: (View) -> Unit
            ) {
                return callback(activity.findViewById(android.R.id.content))
            }

            fun ofFragment(fragment: Fragment, callback: (View) -> Unit) {
                val observer = Observer<LifecycleOwner> { t ->
                    t?.lifecycle?.addObserver(object : DefaultLifecycleObserver {
                        override fun onCreate(owner: LifecycleOwner) {
                            owner.lifecycle.removeObserver(this)
                            callback(fragment.requireView())
                        }

                        override fun onDestroy(owner: LifecycleOwner) {
                            owner.lifecycle.removeObserver(this)
                        }
                    })
                }
                fragment.viewLifecycleOwnerLiveData.observe(fragment, observer)
            }
        }
    }
}

inline fun <reified T : ViewModel> ViewModelStoreOwner.getViewModel(): T {
    return ViewModelProvider(this, SavableViewModelFactory(this as SavedStateRegistryOwner))
        .get(T::class.java).also {
            if (this is ViewModelRegistrable) registry(it)
        }
}

inline fun <reified T : ViewModel> FragmentActivity.viewModel(): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE) { getViewModel<T>() }


inline fun <reified T : ViewModel> Fragment.viewModel(): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE) { getViewModel<T>() }

inline fun <reified T : ViewModel> Fragment.shareViewModel(): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE) { requireActivity().getViewModel<T>() }

inline fun <reified T : ViewModel> viewModel(crossinline function: () -> ViewModelStoreOwner): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE) { function().getViewModel<T>() }

fun ViewModel.launch(
    loading: LoadingEvent? = null,
    error: ErrorEvent? = null,
    context: CoroutineContext = EmptyCoroutineContext,
    function: suspend CoroutineScope.() -> Unit
) {
    val handler = CoroutineExceptionHandler { _, throwable ->
        if (throwable !is CancellationException) {
            throwable.printStackTrace()
            error?.post(throwable)
        }
    }
    viewModelScope.launch(context = context + handler) {
        try {
            loading?.post(true)
            function()
        } finally {
            loading?.post(false)
        }
    }
}