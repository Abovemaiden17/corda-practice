package com.practice.states

import com.practice.contracts.UserContract
import com.practice.schemas.UserSchema
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.AbstractParty
import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import net.corda.core.schemas.QueryableState

@BelongsToContract(UserContract::class)
data class UserState(val name: String,
                     val passwordHash: String,
                     val passwordSalt: String,
                     val jwtToken: String?,
                     override val linearId: UniqueIdentifier,
                     override val participants: List<AbstractParty>): LinearState, QueryableState
{
    fun loginUser(jwtToken: String): UserState = copy(jwtToken = jwtToken)
    fun changePassword(passwordHash: String, passwordSalt: String): UserState =
            copy(passwordSalt = passwordSalt, passwordHash = passwordHash)

    override fun generateMappedObject(schema: MappedSchema): PersistentState = when (schema) {
        is UserSchema -> UserSchema.PersistentUser(
                name = name,
                linearId = linearId.id
        )
        else -> throw IllegalStateException("Cannot construct instance of ${this.javaClass} from Schema: $schema")
    }

    override fun supportedSchemas() = listOf(UserSchema)
}

