package com.dev.coding.functional
import com.dev.coding.views.widget.dialog.ErrorDialogOwner
import java.lang.Exception

interface NotSupportable : ErrorDialogOwner {

    fun notSupport() {
        errorDialog.show(Exception("Not support yet!"))
    }
}
