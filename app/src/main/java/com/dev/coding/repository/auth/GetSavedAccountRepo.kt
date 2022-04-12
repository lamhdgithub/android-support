package com.dev.coding.repository.auth

import android.support.core.di.Inject
import android.support.core.di.ShareScope
import com.dev.coding.datasource.local.UserLocalSource
import com.dev.coding.factory.AccountFactory
import com.dev.coding.model.ui.LoginForm

@Inject(ShareScope.Fragment)
class GetSavedAccountRepo(
		private val userLocalSource: UserLocalSource,
		private val accountFactory: AccountFactory
) {
		operator fun invoke(): LoginForm {
				val accountEntity = userLocalSource.getAccount()
				return accountFactory.createForm(accountEntity)
		}
}