package com.practice.services.interfaces

import com.practice.model.ChangePasswordUserModel
import com.practice.model.RegisterUserModel

interface IUserService : IService {
    fun registerUser(request: RegisterUserModel): String
}