package com.dev.coding.base


import android.support.core.event.WindowStatusOwner
import android.support.core.extensions.LifecycleSubscriberExt
import android.support.core.route.RouteDispatcher
import android.support.core.viewmodel.ViewModelRegistrable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.dev.coding.exception.ErrorHandler
import com.dev.coding.exception.ErrorHandlerImpl
import com.dev.coding.functional.NotSupportable
import com.dev.coding.views.widget.dialog.ConfirmDialogOwner
import com.dev.coding.views.widget.dialog.ErrorDialogOwner
import com.dev.coding.views.widget.dialog.loading.LoadingDialog

abstract class BaseActivity(contentLayoutId: Int) : AppCompatActivity(contentLayoutId),
		LifecycleSubscriberExt,
		ViewModelRegistrable, RouteDispatcher,
		ErrorDialogOwner,
		NotSupportable,
		ErrorHandler by ErrorHandlerImpl() {
		
		private val loadingDialog by lazy { LoadingDialog(this, this) }
		
		override fun onRegistryViewModel(viewModel: ViewModel) {
				if (viewModel is WindowStatusOwner) {
						viewModel.error.bind { handle(this, it) }
						viewModel.loading.bind(loadingDialog::show)
				}
		}
}