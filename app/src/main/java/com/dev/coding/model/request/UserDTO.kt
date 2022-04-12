package com.dev.coding.model.request

data class UserDTO(
		val gender: String? = "",
		val email: String? = "",
		val phone: String? = "",
		val cell: String? = "",
		val name: Name? = null,
		val picture: Picture? = null,
		val login: Login? = null,
		val dob: DOB? = null,
		val location: Location? = null
)

data class Name(
		val title: String? = "",
		val first: String? = "",
		val last: String? = ""
)

data class Picture(
		val large: String? = "",
		val medium: String? = "",
		val thumbnail: String? = ""
)

data class Login(
		val username: String? = "",
		val password: String? = ""
)

data class DOB(
		val date: String? = "",
		val age: String? = ""
)

data class Location(
		val street: Street? = null,
		val city: String? = "",
		val state: String? = "",
		val postcode: String? = ""
)

data class Street(
		val number: String? = "",
		val name: String? = ""
)