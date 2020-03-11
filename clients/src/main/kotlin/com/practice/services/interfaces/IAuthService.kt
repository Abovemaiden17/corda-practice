package com.practice.services.interfaces

import com.practice.model.ChangePasswordUserModel
import com.practice.model.LoginUserModel

interface IAuthService {
    fun loginUser(request: LoginUserModel): Boolean
    fun changePassword(userUID: String, request: ChangePasswordUserModel): Boolean
}