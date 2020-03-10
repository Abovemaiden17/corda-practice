package com.practice.dto

import com.practice.states.UserState

data class UserDTO(
        val name: String,
        val linearId: String
)

fun mapToUserDTO(userState: UserState): UserDTO
{
    return UserDTO(
            name = userState.name,
            linearId = userState.linearId.toString()
    )
}

