package com.dev.coding

import android.app.Application
import android.support.core.di.dependencies
import com.dev.coding.app.appModule

@Suppress("unused")
class MainApplication : Application() {
		
		override fun onCreate() {
				super.onCreate()
				dependencies {
						modules(appModule)
				}
		}
}
