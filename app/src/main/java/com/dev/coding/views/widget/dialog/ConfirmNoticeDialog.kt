package com.dev.coding.views.widget.dialog

import android.content.Context
import android.support.core.view.ViewScopeOwner
import com.dev.coding.base.BaseDialog
import com.dev.coding.databinding.DialogNoticeConfirmBinding
import com.dev.coding.extension.onClick


class ConfirmNoticeDialog(context: Context) : BaseDialog(context) {
		private val binding = viewBinding(DialogNoticeConfirmBinding::inflate)
		
		init {
				binding.btnCancel.onClick {
						dismiss()
				}
		}
		
		fun show(
				title: String,
				message: String,
				function: () -> Unit = {}
		) {
				binding.txtTitle.text = title
				binding.txtBody.text = message
				binding.btnDismiss.onClick {
						function()
						dismiss()
				}
				super.show()
		}
}

interface ConfirmDialogOwner : ViewScopeOwner {
		val confirmDialog: ConfirmNoticeDialog
				get() = with(viewScope) {
						getOr("confirm:dialog") { ConfirmNoticeDialog(context) }
				}
}