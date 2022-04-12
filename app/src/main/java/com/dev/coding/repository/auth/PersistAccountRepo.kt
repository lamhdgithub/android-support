package com.dev.coding.repository.auth

import android.support.core.di.Inject
import android.support.core.di.ShareScope
import com.dev.coding.datasource.local.UserLocalSource
import com.dev.coding.model.entity.AccountEntity
import com.dev.coding.model.ui.LoginForm

@Inject(ShareScope.Fragment)
class PersistAccountRepo(private val userLocalSource: UserLocalSource) {
		suspend operator fun invoke(form: LoginForm) {
				if (form.save) {
						userLocalSource.saveAccount(AccountEntity(form.email, form.password))
				} else {
						userLocalSource.clearAccount()
				}
		}
}