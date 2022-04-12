package com.dev.coding.helper.interceptor

import android.support.core.di.Inject
import android.support.core.di.ShareScope
import android.util.Log
import com.dev.coding.datasource.local.UserLocalSource
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class NoTokenRequired

@Inject(ShareScope.Singleton)
class TokenInterceptor(private val userLocalSource: UserLocalSource) : Interceptor {
		override fun intercept(chain: Interceptor.Chain): Response {
				val originRequest = chain.request()
				val isNoTokenRequired = originRequest.tag(Invocation::class.java)?.method()
						?.getAnnotation(NoTokenRequired::class.java) != null
				if (isNoTokenRequired) return chain.proceed(originRequest)
				
				var request = originRequest
				val token = userLocalSource.getToken()
				if (token.isNotEmpty()) {
						val bearer = "Bearer $token"
						Log.e("Token", bearer)
						request = request.newBuilder()
								.addHeader("Authorization", bearer)
								.build()
				}
				return chain.proceed(request)
		}
}
