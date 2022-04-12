package com.dev.coding.base

import android.support.core.event.WindowStatusOwner
import android.support.core.extensions.LifecycleSubscriberExt
import android.support.core.route.ActivityResultRegister
import android.support.core.route.RouteDispatcher
import android.support.core.viewmodel.ViewModelRegistrable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.dev.coding.app.AppPermission
import com.dev.coding.exception.ErrorHandler
import com.dev.coding.exception.ErrorHandlerImpl
import com.dev.coding.views.widget.dialog.ConfirmDialogOwner
import com.dev.coding.views.widget.dialog.loading.LoadingDialog

abstract class BaseFragment(contentLayoutId: Int) : Fragment(contentLayoutId),
		RouteDispatcher,
		LifecycleSubscriberExt,
		ActivityResultRegister,
		ViewModelRegistrable,
		ConfirmDialogOwner,
		ErrorHandler by ErrorHandlerImpl() {
		val self get() = this
		val appActivity get() = activity as BaseActivity
		private val loadingDialog by lazy { LoadingDialog(requireContext(), this) }
		val appPermission by lazy { AppPermission(this) }
		
		override fun onRegistryViewModel(viewModel: ViewModel) {
				if (viewModel is WindowStatusOwner) {
						viewModel.error.bind { handle(this, it) }
						viewModel.loading.bind(loadingDialog::show)
				}
		}
}