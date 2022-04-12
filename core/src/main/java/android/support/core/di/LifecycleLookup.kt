package android.support.core.di

import androidx.lifecycle.LifecycleOwner

internal interface LifecycleLookup {
    val owner: LifecycleOwner
}