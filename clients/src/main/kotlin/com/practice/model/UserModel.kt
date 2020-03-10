package com.practice.model

import com.fasterxml.jackson.annotation.JsonCreator

data class RegisterUserModel @JsonCreator constructor(
        val name: String
)