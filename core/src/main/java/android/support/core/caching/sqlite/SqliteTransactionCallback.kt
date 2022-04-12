package android.support.core.caching.sqlite

fun interface SqliteTransactionCallback {
    fun onCommitSucceed(tableName: String)
}