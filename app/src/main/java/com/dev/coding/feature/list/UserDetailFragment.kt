package com.dev.coding.feature.list

import android.os.Bundle
import android.support.core.route.argument
import android.support.core.view.viewBinding
import android.support.core.viewmodel.launch
import android.support.core.viewmodel.viewModel
import android.view.View
import androidx.lifecycle.ViewModel
import com.dev.coding.R
import com.dev.coding.base.BaseFragment
import com.dev.coding.databinding.FragmentUserDetailBinding
import com.dev.coding.navigate.Routing
import com.dev.coding.repository.user.FetchUserDetailRepo
import com.dev.coding.views.widget.topbar.SimpleTopBarState
import com.dev.coding.views.widget.topbar.TopBarOwner

class UserDetailFragment : BaseFragment(R.layout.fragment_user_detail), TopBarOwner {
		private val binding by viewBinding(FragmentUserDetailBinding::bind)
		private val args by lazy { argument<Routing.UserDetail>() }
		private val viewModel by viewModel<UserDetailViewModel>()
		
		override fun onCreate(savedInstanceState: Bundle?) {
				super.onCreate(savedInstanceState)
				if (savedInstanceState == null) viewModel.setEmail(args.email)
		}
		
		override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
				super.onViewCreated(view, savedInstanceState)
				viewModel.user.bind {
						with(binding) {
								imgAvatar.setImageUrl(it.avatar)
								txtName.text = it.fullName
								txtEmail.text = it.email
								txtUsername.text = it.userName
								txtPassword.text = it.password
								txtGender.text = it.gender
								txtAge.text = it.age
								txtPhone.text = it.phone
								txtCellPhone.text = it.cellPhone
								txtStreet.text = it.street
								txtCity.text = it.city
								txtState.text = it.state
								txtPostcode.text = it.postCode
						}
				}
		}
		
		override fun onStart() {
				super.onStart()
				topBar.setState(
						SimpleTopBarState(
								title = R.string.title_user_details,
								hasDivider = true,
								onBackClick = { requireActivity().onBackPressed() }
						)
				)
		}
}

class UserDetailViewModel(private val fetchUserDetailRepo: FetchUserDetailRepo) : ViewModel() {
		val user = fetchUserDetailRepo.result
		
		fun setEmail(email: String) = launch {
				fetchUserDetailRepo(email)
		}
}