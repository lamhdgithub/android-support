package android.support.core.caching

interface Parser {
    fun <T> fromJson(string: String?, type: Class<T>): T?
    fun <T> toJson(value: T?): String
}