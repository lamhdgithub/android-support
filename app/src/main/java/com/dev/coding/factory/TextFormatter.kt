package com.dev.coding.factory

import android.content.Context
import android.support.core.di.Inject
import android.support.core.di.ShareScope
import com.dev.coding.R
import com.dev.coding.model.request.Name
import com.dev.coding.model.request.Street

@Inject(ShareScope.Singleton)
class TextFormatter(private val context: Context) {
		fun fullName(name: Name?): String {
				name ?: return ""
				return "${name.title}.${name.first} ${name.last}"
		}
		
		fun userName(username: String?): String {
				return context.getString(R.string.label_username, username)
		}
		
		fun password(password: String?): String {
				return context.getString(R.string.label_password, password)
		}
		
		fun age(age: String?): String {
				return context.getString(R.string.label_age, age)
		}
		
		fun street(street: Street?): String {
				return context.getString(R.string.label_street, street?.number, street?.name)
		}
		
		fun city(city: String?): String {
				return context.getString(R.string.label_city, city)
		}
		
		fun state(state: String?): String {
				return context.getString(R.string.label_state, state)
		}
		
		fun postCode(postCode: String?): String {
				return context.getString(R.string.label_post_code, postCode)
		}
		
		fun cellPhone(cell: String?): String {
				return context.getString(R.string.label_cell_phone, cell)
		}
		
		fun gender(convert: Gender): String {
				return context.getString(R.string.label_gender, convert)
		}
		
		fun email(email: String?): String {
				return context.getString(R.string.label_email, email)
		}
		
		fun phone(phone: String?): String {
				return context.getString(R.string.label_phone, phone)
		}
}