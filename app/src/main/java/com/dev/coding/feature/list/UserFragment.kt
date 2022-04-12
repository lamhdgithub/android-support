package com.dev.coding.feature.list

import android.os.Bundle
import android.support.core.event.LiveDataStatusOwner
import android.support.core.event.WindowStatusOwner
import android.support.core.view.viewBinding
import android.support.core.viewmodel.launch
import android.support.core.viewmodel.viewModel
import android.view.View
import androidx.lifecycle.ViewModel
import com.dev.coding.R
import com.dev.coding.base.BaseRefreshFragment
import com.dev.coding.databinding.FragmentUserBinding
import com.dev.coding.extension.show
import com.dev.coding.model.ui.IUserDTOOwner
import com.dev.coding.navigate.Route
import com.dev.coding.repository.user.FetchAllUsersRepo
import com.dev.coding.views.widget.topbar.TextCenterTopBarState
import com.dev.coding.views.widget.topbar.TopBarOwner

class UserFragment : BaseRefreshFragment(R.layout.fragment_user), TopBarOwner {
		private val binding by viewBinding(FragmentUserBinding::bind)
		private val viewModel by viewModel<UserViewModel>()
		override fun onRefreshListener() {
				viewModel.refresh()
		}
		
		override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
				super.onViewCreated(view, savedInstanceState)
				viewModel.users.bind(UserAdapter(binding.rcvUser)
						.apply {
						onItemClickListener = {
								Route.openUserDetail(self, (it as IUserDTOOwner).value?.email!!)
						}
				}::submit)
				
				viewModel.users.bind {
						it.isNullOrEmpty() show binding.noData
						!it.isNullOrEmpty() show binding.rcvUser
				}
		}
		
		override fun onStart() {
				super.onStart()
				topBar.setState(
						TextCenterTopBarState(
								title = R.string.title_users,
								hasDivider = true
						)
				)
		}
}

class UserViewModel(
		private val fetchAllUsersRepo: FetchAllUsersRepo
) : ViewModel(), WindowStatusOwner by LiveDataStatusOwner() {
		val users = fetchAllUsersRepo.result
		
		init {
				refresh()
		}
		
		fun refresh() = launch(refreshLoading, error) {
				fetchAllUsersRepo()
		}
}
