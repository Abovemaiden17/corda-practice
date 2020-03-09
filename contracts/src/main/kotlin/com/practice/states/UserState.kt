package com.practice.states

import com.practice.contracts.UserContract
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.AbstractParty

@BelongsToContract(UserContract::class)
data class UserState(val name: String,
                     override val linearId: UniqueIdentifier,
                     override val participants: List<AbstractParty>): LinearState