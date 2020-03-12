package com.practice.services

import com.practice.common.ConvertFunctions
import com.practice.common.UserConstants
import com.practice.common.appexceptions.NotFoundException
import com.practice.common.appexceptions.ValidationTracker
import com.practice.dto.mapToUserDTO
import com.practice.flows.user.RegisterUserFlow
import com.practice.model.RegisterUserModel
import com.practice.services.interfaces.IUserService
import com.practice.states.UserState
import com.practice.utils.password.PasswordEncryption
import com.practice.utils.password.PasswordManager
import com.practice.webserver.NodeRPCConnection
import org.springframework.stereotype.Service

@Service
class UserService (private val rpc: NodeRPCConnection, private val func: ConvertFunctions): IUserService {
    override fun getAll(): Any {
        val users = rpc.proxy.vaultQuery(UserState::class.java).states
        return users.map { mapToUserDTO(it.state.data) }
    }

    override fun get(linearId: String): Any {
        val id = func.stringToUniqueIdentifier(linearId)
        val userState= rpc.proxy.vaultQuery(UserState::class.java).states.find {
            it.state.data.linearId == id
        } ?: throw NotFoundException("User with $linearId cannot be found")
        return mapToUserDTO(userState = userState.state.data)
    }

    override fun registerUser(request: RegisterUserModel): String {
        // Validate client request
        val tracker = ValidationTracker()

        if(request.name.isBlank() || request.name.isEmpty())
            tracker.add("name", "Name cannot be empty")

        if(request.password.isBlank() || request.password.isEmpty())
            tracker.add("password", "Password cannot be empty")

        tracker.validate()

        // Encrypt password
        val passwordSalt = PasswordManager().generatePassword(true, isWithUppercase = true, isWithNumbers = true, isWithSpecial = false, length = 32)
        val passwordHash = PasswordEncryption(passwordSalt, UserConstants.SECRET_KEY).encryption.encrypt(request.password)

        val userFlow = rpc.proxy.startFlowDynamic(
                RegisterUserFlow::class.java,
                request.name,
                passwordHash,
                passwordSalt
        )
        return userFlow.returnValue.get().coreTransaction.outRefsOfType<UserState>().single().state.data.linearId.toString()

    }

}