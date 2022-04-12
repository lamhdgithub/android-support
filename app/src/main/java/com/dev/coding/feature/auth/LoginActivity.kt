package com.dev.coding.feature.auth

import android.os.Bundle
import android.os.Parcelable
import android.support.core.event.LiveDataStatusOwner
import android.support.core.event.LoadingEvent
import android.support.core.event.WindowStatusOwner
import android.support.core.livedata.LoadingLiveData
import android.support.core.livedata.SingleLiveEvent
import android.support.core.livedata.post
import android.support.core.route.close
import android.support.core.route.closeSuccess
import android.support.core.view.viewBinding
import android.support.core.viewmodel.launch
import android.support.core.viewmodel.viewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStateSaveAble
import androidx.lifecycle.lifecycleScope
import com.dev.coding.R
import com.dev.coding.base.BaseActivity
import com.dev.coding.databinding.ActivityLoginBinding
import com.dev.coding.extension.bind
import com.dev.coding.helper.ApplicationScope
import com.dev.coding.model.ui.IAccount
import com.dev.coding.model.ui.LoginForm
import com.dev.coding.navigate.Route
import com.dev.coding.repository.auth.GetSavedAccountRepo
import com.dev.coding.repository.auth.LoginRepo
import com.dev.coding.repository.auth.PersistAccountRepo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginActivity : BaseActivity(R.layout.activity_login) {
		
		private val binding by viewBinding(ActivityLoginBinding::bind)
		private val viewModel by viewModel<LoginViewModel>()
		
		override fun onCreate(savedInstanceState: Bundle?) {
				super.onCreate(savedInstanceState)
				with(binding) {
						viewModel.form.run {
								edtPassword.bind(::password::set)
								edtUserName.bind(::email::set)
								cbSaveAccount.bind(::save::set)
						}
				}
				
				binding.btnLogin.setOnClickListener {
						lifecycleScope.launch {
								viewModel.login()
						}
				}
				
				with(viewModel) {
						viewLoading.bind(binding.btnLogin::setEnabled) { !this }
						account.bind(binding.edtPassword::setText) { password }
						account.bind(binding.edtUserName::setText) { email }
						account.bind(binding.cbSaveAccount::setChecked) { save }
						loginSuccess.bind { Route.run { redirectToMain() } }
				}
		}
}

class LoginViewModel(
		private val persistAccountRepo: PersistAccountRepo,
		private val getSavedAccountRepo: GetSavedAccountRepo,
		private val loginRepo: LoginRepo
) : ViewModel(),
		WindowStatusOwner by LiveDataStatusOwner(),
		ViewModelStateSaveAble {
		
		var form: LoginForm = LoginForm()
				private set
		val account = MutableLiveData<IAccount>()
		val viewLoading: LoadingEvent = LoadingLiveData()
		val loginSuccess = SingleLiveEvent<Int>()
		
		init {
				launch {
						form = getSavedAccountRepo()
						account.post(form)
				}
		}
		
		fun login() = launch(loading, error) {
				loginRepo(form)
				delay(1000)
				loginSuccess.post(R.string.msg_login_success)
		}
		
		override fun onCleared() {
				super.onCleared()
				
				ApplicationScope.launch {
						persistAccountRepo(form)
				}
		}
		
		override fun saveState(): Parcelable {
				return form
		}
		
		override fun restoreState(savedState: Parcelable) {
				form = savedState as LoginForm
				account.post(form)
		}
}