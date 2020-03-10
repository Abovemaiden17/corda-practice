package com.practice.flows

import com.practice.contracts.UserContract
import com.practice.contracts.UserContract.Companion.USER_ID
import com.practice.functions.UserFunctions
import com.practice.states.UserState
import net.corda.core.CordaException
import net.corda.core.contracts.Command
import net.corda.core.contracts.StateAndRef
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.StartableByRPC
import net.corda.core.transactions.TransactionBuilder

@StartableByRPC
class RegisterUserFlow (private val name: String): UserFunctions()
{
    override fun call(): StateAndRef<UserState> {
        val transaction = transaction()
        val ptx = verifyAndSign(transaction)
        val stx = recordTransactionWithoutCounterParty(ptx)
        return stx.coreTransaction.outRefsOfType(UserState::class.java).single()
    }

    private fun userState(): UserState
    {
        return UserState(
                name = name,
                linearId = UniqueIdentifier(),
                participants = listOf(ourIdentity)
        )
    }

    private fun transaction(): TransactionBuilder {
        val notary = notary()
        val userState = userState()
        val builder = TransactionBuilder(notary = notary)

        val registerUserCmd = Command(
                value = UserContract.Commands.Register(),
                signers = userState.participants.map { it.owningKey }
        )

        builder.addCommand(registerUserCmd)
        builder.addOutputState(userState, USER_ID)

        return builder
    }
}