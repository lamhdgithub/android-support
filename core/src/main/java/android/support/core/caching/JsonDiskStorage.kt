package android.support.core.caching

import android.content.Context
import android.support.core.CoDispatcher
import android.support.core.extensions.withIO
import android.support.core.livedata.mapNotNull
import android.support.core.livedata.post
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.locks.ReentrantLock
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

class JsonDiskStorage<T : Any>(
    private val name: String,
    private val context: Context,
    private val type: KClass<T>,
    private val keyOf: (T) -> String
) : DiskStorage<T>, CoroutineScope {
    override val coroutineContext: CoroutineContext = Job() + CoDispatcher.io

    private val parser = Gson()
    private val mShared = context.getSharedPreferences(name, Context.MODE_PRIVATE)
    private val mCache = hashMapOf<String, T>()
    private val mData = MutableLiveData<List<T>>()
    private val mLock = ReentrantLock()

    init {
        launch {
            try {
                lockCache {
                    mShared.all.forEach {
                        if (it.value != null) {
                            mCache[it.key] = parser.fromJson(it.value as String, type.java)
                        }
                    }
                }
                notifyDatasetChange()
            } catch (e: Throwable) {
            }
        }
    }

    private suspend fun notifyDatasetChange() {
        if (!mLock.isLocked) mData.post(findAll())
    }

    override suspend fun findAll(): List<T> {
        return lockCache { mCache.values.toList() }
    }

    override suspend fun findAllWith(options: QueryOptions): PagingList<T> {
        error("Not support ${javaClass.name} yet!")
    }

    override suspend fun findAllByIds(ids: List<String>): List<T> {
        return lockCache { ids.mapNotNull { mCache[it] } }
    }

    override suspend fun findById(id: String): T? {
        return lockCache { mCache[id] }
    }

    override fun getAll(): LiveData<List<T>> {
        return mData
    }

    override fun getById(id: String): LiveData<T> {
        return mData.mapNotNull { lockCache { mCache[id] } }
    }

    override suspend fun saveAll(items: Collection<T>?) {
        items ?: return
        withIO {
            mShared.edit {
                lockCache {
                    items.forEach {
                        val key = keyOf(it)
                        putString(key, parser.toJson(it))
                        mCache[key] = it
                    }
                }
            }

            notifyDatasetChange()
        }
    }

    override suspend fun remove(id: String?) {
        id ?: return
        withIO {
            mShared.edit { remove(id) }
            lockCache { mCache.remove(id) }
            notifyDatasetChange()
        }
    }

    override suspend fun save(item: T) {
        val id = keyOf(item)
        withIO {
            mShared.edit { putString(id, parser.toJson(item)) }
            lockCache { mCache[id] = item }
            notifyDatasetChange()
        }
    }

    override suspend fun removeAll() {
        withIO {
            mShared.edit { this.clear() }
            lockCache { mCache.clear() }
            notifyDatasetChange()
        }
    }

    override suspend fun count(): Int {
        return lockCache { mCache.size }
    }

    private fun <T> lockCache(function: () -> T): T {
        return synchronized(mCache) { function() }
    }

    override suspend fun transaction(transaction: suspend DiskWriteable<T>.() -> Unit) {
        mLock.lock()

        val result = kotlin.runCatching {
            transaction()
        }
        mLock.unlock()

        if (!result.isFailure) {
            notifyDatasetChange()
        } else {
            throw result.exceptionOrNull()!!
        }
    }

    override fun getAllWith(options: QueryOptions): LiveData<PagingList<T>> {
        error("Not support ${javaClass.name} yet!")
    }

}