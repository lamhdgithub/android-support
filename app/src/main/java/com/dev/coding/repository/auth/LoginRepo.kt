package com.dev.coding.repository.auth

import android.support.core.di.Inject
import android.support.core.di.ShareScope
import com.dev.coding.datasource.local.UserLocalSource
import com.dev.coding.datasource.remote.AuthenticateApi
import com.dev.coding.model.ui.LoginForm

@Inject(ShareScope.Activity)
class LoginRepo(
		private val userLocalSource: UserLocalSource,
		private val authenticateApi: AuthenticateApi
) {
		suspend operator fun invoke(form: LoginForm) {
				form.validate()
				val token = authenticateApi.login(form.email, form.password).await()
				userLocalSource.saveToken(token)
		}
}
