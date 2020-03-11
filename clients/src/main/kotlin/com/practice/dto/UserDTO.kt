package com.practice.dto

import com.practice.states.UserState

data class UserDTO(
        val name: String,
        val jwtToken: String?,
        val linearId: String
)

fun mapToUserDTO(userState: UserState): UserDTO
{
    return UserDTO(
            name = userState.name,
            jwtToken = userState.jwtToken,
            linearId = userState.linearId.toString()
    )
}

