package android.support.core.viewmodel

import android.support.core.ComposeData
import android.support.core.ComposeDataImpl
import android.support.core.event.Event
import android.support.core.extensions.SubscriberExt
import android.support.core.livedata.post
import android.support.core.livedata.LoadingLiveData
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

interface ViewModelSubscriberExt : SubscriberExt {
    private val viewModelScope get() = (this as ViewModel).viewModelScope

    override fun <T> Flow<T>.bind(observer: Observer<in T>) {
        viewModelScope.launch {
            collect(observer::onChanged)
        }
    }

    override fun <T> LiveData<T>.bind(observer: Observer<in T>) {
        viewModelScope.launch {
            suspendCancellableCoroutine<T> {
                it.invokeOnCancellation { removeObserver(observer) }
                observeForever(observer)
            }
        }
    }

    override fun <T> Event<T>.bind(observer: Observer<T>) {
        error("Not support bind for event")
    }

    suspend fun <T> CoroutineScope.with(
        loading: LoadingLiveData? = null,
        error: LiveData<Throwable>? = null,
        function: suspend CoroutineScope.() -> T
    ): T {
        return try {
            loading?.post(true)
            function(this)
        } catch (e: Throwable) {
            (error as? MutableLiveData)?.post(e)
            throw e
        } finally {
            loading?.post(false)
        }
    }

    fun <V> awaitAll(
        vararg liveData: LiveData<out Any>,
        function: suspend CoroutineScope.(ComposeData) -> V
    ): LiveData<V> {
        val next = MediatorLiveData<V>()
        val composeData = ComposeDataImpl(liveData.size)
        composeData.awaitAll {
            viewModelScope.launch { next.post(function(it)) }
        }
        liveData.forEachIndexed { index, liveDatum ->
            next.addSource(liveDatum) {
                composeData.put(index, it)
            }
        }
        return next
    }
}