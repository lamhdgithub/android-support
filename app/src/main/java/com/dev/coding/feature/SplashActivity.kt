package com.dev.coding.feature

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.core.livedata.call
import android.support.core.viewmodel.launch
import android.support.core.viewmodel.viewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dev.coding.R
import com.dev.coding.base.BaseActivity
import com.dev.coding.navigate.Route
import com.dev.coding.repository.auth.CheckLoginRepo

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity(R.layout.activity_splash) {
    private val viewModel by viewModel<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.main.bind {
            Route.run { redirectToMain() }
        }
        viewModel.login.bind {
            Route.run { redirectToLogin() }
        }
    }
}

class SplashViewModel(private val checkLoginRepo: CheckLoginRepo) : ViewModel() {
    val login = MutableLiveData<Any>()
    val main = MutableLiveData<Any>()

    init {
        launch {
            if (checkLoginRepo()) {
                main.call()
            } else {
                login.call()
            }
        }
    }
}