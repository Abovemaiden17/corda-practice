package com.practice.services

import com.practice.common.appexceptions.NotFoundException
import com.practice.dto.mapToUserDTO
import com.practice.flows.RegisterUserFlow
import com.practice.model.RegisterUserModel
import com.practice.services.interfaces.IUserService
import com.practice.states.UserState
import com.practice.webserver.NodeRPCConnection
import net.corda.core.contracts.UniqueIdentifier
import org.springframework.stereotype.Service

@Service
class UserService (private val rpc: NodeRPCConnection): IUserService {
    override fun getAll(): Any {
        val users = rpc.proxy.vaultQuery(UserState::class.java).states
        return users.map { mapToUserDTO(it.state.data) }
    }

    override fun get(linearId: String): Any {
        val id = stringToUniqueIdentifier(linearId)
        val userState= rpc.proxy.vaultQuery(UserState::class.java).states.find {
            it.state.data.linearId == id
        } ?: throw NotFoundException("User with $linearId cannot be found")
        return mapToUserDTO(userState = userState.state.data)
    }

    override fun registerUser(request: RegisterUserModel): String {
        val userFlow = rpc.proxy.startFlowDynamic(
                RegisterUserFlow::class.java,
                request.name
        )
        return userFlow.returnValue.get().state.data.linearId.toString()
    }

    private fun stringToUniqueIdentifier(id: String): UniqueIdentifier
    {
        return UniqueIdentifier.fromString(id)
    }
}