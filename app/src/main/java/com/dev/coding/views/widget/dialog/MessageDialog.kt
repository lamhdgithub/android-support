package com.dev.coding.views.widget.dialog

import android.content.Context
import com.dev.coding.base.BaseDialog
import com.dev.coding.databinding.DialogMessageBinding
import com.dev.coding.extension.onClick
import com.dev.coding.extension.show

open class MessageDialog(context: Context) : BaseDialog(context) {
    private var mOnDismiss: () -> Unit = {}
    private val binding = viewBinding(DialogMessageBinding::inflate)

    init {
        binding.btnDismiss.onClick {
            dismiss()
            mOnDismiss()
        }
    }

    fun show(title: String, message: String, onDismiss: () -> Unit = {}) {
        mOnDismiss = onDismiss
        binding.txtBody.show(message.isNotBlank()) {
            text = message
        }
        binding.txtTitle.show(title.isNotBlank()) {
            text = title
        }
        super.show()
    }

    override fun show() {
        error("Not support")
    }
}