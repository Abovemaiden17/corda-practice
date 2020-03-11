package com.practice.model

import com.fasterxml.jackson.annotation.JsonCreator

data class RegisterUserModel @JsonCreator constructor(
        val name: String,
        val password: String
)

data class LoginUserModel @JsonCreator constructor(
        val username: String,
        val password: String
)

data class ChangePasswordUserModel @JsonCreator constructor(
        val oldPassword: String,
        val newPassword: String
)