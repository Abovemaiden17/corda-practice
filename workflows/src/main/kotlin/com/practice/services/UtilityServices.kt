package com.practice.services

import com.practice.states.UserState
import net.corda.core.CordaException
import net.corda.core.contracts.StateAndRef
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.CordaX500Name
import net.corda.core.identity.Party
import net.corda.core.node.AppServiceHub
import net.corda.core.node.services.CordaService
import net.corda.core.node.services.queryBy
import net.corda.core.node.services.vault.QueryCriteria
import net.corda.core.serialization.SingletonSerializeAsToken

@CordaService
class UtilityServices (private val serviceHub: AppServiceHub): SingletonSerializeAsToken() {
    init { }

    fun getNotary(): Party {
        val config = serviceHub.getAppContext().config
        val value = config.getString("notary")
        return stringToParty(value)
    }

    // Get user
    fun getUserById(userUID: String): StateAndRef<UserState> {
        val linearId = stringToUniqueIdentifier(userUID)
        val criteria = QueryCriteria.LinearStateQueryCriteria(linearId = listOf(linearId))
        return serviceHub.vaultService.queryBy<UserState>(criteria = criteria).states.singleOrNull()
                ?: throw CordaException("Cannot find User State with $userUID")
    }

    private fun stringToUniqueIdentifier(id: String): UniqueIdentifier {
        return UniqueIdentifier.fromString(id)
    }

    // Conversion of string to a Party
    private fun stringToParty(name: String): Party {
        return serviceHub.identityService.wellKnownPartyFromX500Name(CordaX500Name.parse(name))
                ?: throw IllegalArgumentException("Unknown party name.")
    }


}