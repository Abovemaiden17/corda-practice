package com.practice.services

import com.practice.schemas.UserSchema
import com.practice.states.UserState
import net.corda.core.contracts.StateAndRef
import net.corda.core.flows.FlowException
import net.corda.core.node.AppServiceHub
import net.corda.core.node.services.CordaService
import net.corda.core.node.services.queryBy
import net.corda.core.node.services.vault.QueryCriteria
import net.corda.core.node.services.vault.builder
import net.corda.core.serialization.SingletonSerializeAsToken
import java.util.*

@CordaService
class UserServices (private val serviceHub: AppServiceHub): SingletonSerializeAsToken() {
    init { }

    fun getUserById(id: String): StateAndRef<UserState> {
        val uuid = stringToUUID(id)
        return builder {
            val query =
                    UserSchema.PersistentUser::linearId.equal(uuid)
            val criteria = QueryCriteria.VaultCustomQueryCriteria(query)
            serviceHub.vaultService.queryBy<UserState>(criteria = criteria).states
        }.firstOrNull() ?: throw FlowException("User state with $id not found")
    }

    private fun stringToUUID(id: String): UUID {
        return UUID.fromString(id)
    }
}