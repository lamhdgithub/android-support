package com.dev.coding.factory

import android.support.core.di.Inject
import android.support.core.di.ShareScope
import com.dev.coding.extension.safe
import com.dev.coding.model.request.UserDTO
import com.dev.coding.model.ui.IUser
import com.dev.coding.model.ui.IUserDTOOwner
import com.dev.coding.model.ui.IUserDetails


@Inject(ShareScope.Singleton)
class UserFactory(
		private val textFormatter: TextFormatter,
		private val genderConverter: GenderConverter
) {
		
		fun create(userDTO: UserDTO): IUser {
				return object : IUser, IUserDTOOwner {
						override val fullName: String
								get() = textFormatter.fullName(userDTO.name)
						
						override val avatar: String
								get() = userDTO.picture?.medium.safe()
						
						override val email: String
								get() = textFormatter.email(userDTO.email)
						
						override val phone: String
								get() = textFormatter.phone(userDTO.phone)
						
						override val value: UserDTO get() = userDTO
				}
		}
		
		fun createDetail(userDTO: UserDTO): IUserDetails {
				return object : IUserDetails, IUser by create(userDTO) {
						override val email: String
								get() = userDTO.email.safe()
						
						override val avatar: String
								get() = userDTO.picture?.large.safe()
						
						override val cellPhone: String
								get() = textFormatter.cellPhone(userDTO.cell)
						
						override val userName: String
								get() = textFormatter.userName(userDTO.login?.username)
						
						override val password: String
								get() = textFormatter.userName(userDTO.login?.password)
						
						override val gender: String
								get() = textFormatter.gender(genderConverter.convert(userDTO.gender))
						
						override val age: String
								get() = textFormatter.userName(userDTO.dob?.age)
						
						override val street: String
								get() = textFormatter.street(userDTO.location?.street)
						
						override val city: String
								get() = textFormatter.city(userDTO.location?.city)
						
						override val state: String
								get() = textFormatter.state(userDTO.location?.state)
						
						override val postCode: String
								get() = textFormatter.postCode(userDTO.location?.postcode)
				}
		}
}