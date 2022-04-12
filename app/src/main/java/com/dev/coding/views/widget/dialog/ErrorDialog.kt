package com.dev.coding.views.widget.dialog

import android.content.Context
import android.support.core.view.ViewScopeOwner

class ErrorDialog(context: Context) : MessageDialog(context) {
		fun show(e: Throwable) {
				show("Error", e.message ?: "Unknown")
		}
}

interface ErrorDialogOwner : ViewScopeOwner {
		val errorDialog: ErrorDialog
				get() = with(viewScope) {
						getOr("error:dialog") { ErrorDialog(context) }
				}
}