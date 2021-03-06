package android.support.core.caching

import android.content.Context
import android.support.core.caching.sqlite.LiveDataTrackingContainer
import android.support.core.caching.sqlite.SqliteDiskStorage
import android.support.core.caching.sqlite.TrackingSqliteOpenHelper
import com.google.gson.Gson
import kotlin.reflect.KClass

interface DiskStorageFactory {
    fun <T : Any> create(
        name: String,
        clazz: KClass<T>,
        groupBy: ((T) -> String)? = null,
        keyOf: (T) -> String,
    ): DiskStorage<T>
}

class SharedDiskStorageFactory(private val context: Context) : DiskStorageFactory {
    override fun <T : Any> create(
        name: String,
        clazz: KClass<T>,
        groupBy: ((T) -> String)?,
        keyOf: (T) -> String
    ): DiskStorage<T> {
        return JsonDiskStorage(name, context, clazz, keyOf)
    }
}

class SqliteDiskStorageFactory(
    private val context: Context,
    private val converter: Gson = Gson(),
    version: Int = 1
) : DiskStorageFactory {
    private val database = TrackingSqliteOpenHelper(context, "disk_storage", version)
    private val liveDataTrackingContainer = LiveDataTrackingContainer()

    override fun <T : Any> create(
        name: String,
        clazz: KClass<T>,
        groupBy: ((T) -> String)?,
        keyOf: (T) -> String
    ): SqliteDiskStorage<T> {
        if (name.isBlank()) error("Name should not be blank")
        return SqliteDiskStorage(
            tableName = name,
            sqlite = database,
            container = liveDataTrackingContainer,
            clazz = clazz.java,
            converter = converter,
            keyOf = keyOf,
            groupOf = groupBy ?: keyOf
        )
    }
}