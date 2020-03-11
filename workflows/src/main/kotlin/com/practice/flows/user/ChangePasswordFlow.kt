package com.practice.flows.user

import com.practice.contracts.UserContract
import com.practice.flows.BaseFlow
import com.practice.states.UserState
import net.corda.core.contracts.Command
import net.corda.core.flows.StartableByRPC
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder

@StartableByRPC
class ChangePasswordFlow (private val userUID: String,
                          private val passwordHash: String,
                          private val passwordSalt: String): BaseFlow()
{
    override fun call(): SignedTransaction {
        val transaction = transaction()
        val ptx = verifyAndSign(transaction)
        return recordTransactionWithoutCounterParty(ptx)
    }

    private fun userState(): UserState {
        val userState = userServices().getUserById(userUID).state.data
        return userState.changePassword(
                passwordHash = passwordHash,
                passwordSalt = passwordSalt
        )
    }

    private fun transaction(): TransactionBuilder {
        val notary = utilityServices().getNotary()
        val userState = userState()
        val builder = TransactionBuilder(notary = notary)

        val changePasswordUserCmd = Command(
                value = UserContract.Commands.Change(),
                signers = userState.participants.map { it.owningKey }
        )

        builder.addCommand(changePasswordUserCmd)
        builder.addOutputState(userState, UserContract.USER_ID)

        return builder
    }
}