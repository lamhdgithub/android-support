package com.dev.coding.model.ui

import com.dev.coding.model.request.UserDTO

interface IUser {
    val avatar: String get() = ""
    val fullName: String get() = ""
    val email: String get() = ""
    val phone: String get() = ""
    
    companion object : IUser
}

interface IUserDTOOwner {
    val value: UserDTO? get() = null
}

interface IUserDetails : IUser {
    val cellPhone: String get() = ""
    val userName: String get() = ""
    val password: String get() = ""
    val gender: String get() = ""
    val age: String get() = ""
    val street: String get() = ""
    val city: String get() = ""
    val state: String get() = ""
    val postCode: String get() = ""
}
