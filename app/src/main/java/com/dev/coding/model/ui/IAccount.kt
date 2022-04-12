package com.dev.coding.model.ui

import android.os.Parcelable
import com.dev.coding.R
import com.dev.coding.exception.viewError
import kotlinx.parcelize.Parcelize

interface IAccount {
    val email: String get() = ""
    val password: String get() = ""
    val save: Boolean get() = false
}

@Parcelize
class LoginForm(
    override var email: String = "",
    override var password: String = "",
    override var save: Boolean = false
) : IAccount, Parcelable {

    fun validate() {
        if (email.isBlank()) viewError(R.id.edtUserName, R.string.error_blank_email)
        if (password.isBlank()) viewError(R.id.edtPassword, R.string.error_blank_password)
    }
}