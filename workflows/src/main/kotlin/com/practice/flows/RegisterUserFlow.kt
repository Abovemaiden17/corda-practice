package com.practice.flows

import com.practice.contracts.UserContract
import com.practice.contracts.UserContract.Companion.USER_ID
import com.practice.functions.CordaFunctions
import com.practice.states.UserState
import net.corda.core.contracts.Command
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.flows.StartableByRPC
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder

@StartableByRPC
class RegisterUserFlow (private val name: String): CordaFunctions()
{
    override fun call(): SignedTransaction {
        val transaction = transaction()
        val stx = verifyAndSign(transaction)
        return recordTransactionWithoutCounterParty(stx)
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