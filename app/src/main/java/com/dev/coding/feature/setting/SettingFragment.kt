package com.dev.coding.feature.setting

import android.os.Bundle
import android.support.core.livedata.SingleLiveEvent
import android.support.core.livedata.call
import android.support.core.view.viewBinding
import android.support.core.viewmodel.launch
import android.support.core.viewmodel.viewModel
import android.view.View
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModel
import com.dev.coding.R
import com.dev.coding.base.BaseFragment
import com.dev.coding.databinding.FragmentSettingBinding
import com.dev.coding.extension.launchImage
import com.dev.coding.extension.onClick
import com.dev.coding.navigate.Route
import com.dev.coding.repository.auth.LogoutRepo
import com.dev.coding.views.widget.topbar.TextCenterTopBarState
import com.dev.coding.views.widget.topbar.TopBarOwner

class SettingFragment : BaseFragment(R.layout.fragment_setting), TopBarOwner {
		private val viewModel by viewModel<SettingViewModel>()
		private val binding by viewBinding(FragmentSettingBinding::bind)
		
		override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
				super.onViewCreated(view, savedInstanceState)
				val content = registerForActivityResult(
						ActivityResultContracts.GetContent(),
						binding.imgAvatar::setImageURI
				)
				with(binding) {
						imgAvatar.onClick{content.launchImage()}
						btnLogout.onClick { viewModel.logout() }
				}
				viewModel.logoutSuccess.bind { Route.run { closeAndOpenLogin() } }
				topBar.setState(TextCenterTopBarState(R.string.title_setting))
		}
		
		class SettingViewModel(private val logoutRepo: LogoutRepo) : ViewModel() {
				val logoutSuccess = SingleLiveEvent<Any>()
				
				fun logout() = launch {
						logoutRepo()
						logoutSuccess.call()
				}
		}
}
