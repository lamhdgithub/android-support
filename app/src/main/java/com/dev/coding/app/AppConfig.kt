package com.dev.coding.app

interface AppConfig {
    
    val endpoint: String get() = "https://randomuser.me/api/"
    val shouldLog: Boolean get() = true

    companion object : AppConfig by BuildModeDelegate.mode
}

private object BuildModeDelegate {
    val mode = when ("dev") {
        "pro" -> switchMode(ProDebug(), ProRelease())
        "dev" -> switchMode(DevDebug(), DevRelease())
        "staging" -> switchMode(StagingDebug(), StagingRelease())
        else -> error("Not support build config")
    }

    private fun switchMode(debug: AppConfig, release: AppConfig): AppConfig {
//        return if (BuildConfig.DEBUG) debug else release
        return if (true) debug else release
    }
}

private open class DevDebug : AppConfig

private open class StagingDebug : AppConfig

private open class ProDebug : AppConfig {
    override val endpoint: String get() = "https://randomuser.me/api/prod"
}

private class ProRelease : ProDebug() {
    override val shouldLog: Boolean get() = false
}

private class DevRelease : DevDebug() {
    override val shouldLog: Boolean get() = true
}

private class StagingRelease : StagingDebug() {
    override val shouldLog: Boolean get() = false
}