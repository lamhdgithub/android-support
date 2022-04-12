package com.dev.coding.exception

import android.app.Activity
import android.support.core.extensions.block
import android.support.core.route.RouteDispatcher
import android.widget.EditText
import com.dev.coding.base.BaseActivity
import com.dev.coding.base.BaseFragment
import com.dev.coding.extension.cast

interface ErrorHandler {
		fun handle(activity: BaseActivity, error: Throwable)
		fun handle(fragment: BaseFragment, error: Throwable)
}

class ErrorHandlerImpl : ErrorHandler {
		
		override fun handle(activity: BaseActivity, error: Throwable) = block(activity) {
				handle(activity, activity as RouteDispatcher, error)
		}
		
		override fun handle(fragment: BaseFragment, error: Throwable) = block(fragment) {
				val activity = fragment.activity.cast<BaseActivity>()
				activity?.let { handle(it, fragment as RouteDispatcher, error) }
						?: error("Fragment should be part of AppActivity")
		}
		
		private fun handle(activity: BaseActivity, dispatcher: RouteDispatcher, error: Throwable) {
				when (error) {
						is ViewError -> {
								val view = if (dispatcher is BaseFragment) {
										dispatcher.requireView().findViewById<EditText>(error.viewId)
								} else {
										(dispatcher as Activity).findViewById(error.viewId)
								}
								view.run {
										this.error = activity.getString(error.res)
										requestFocus()
								}
								return
						}
						else -> activity.errorDialog.show(error)
				}
		}
}

