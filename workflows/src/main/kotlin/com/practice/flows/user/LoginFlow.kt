package com.practice.flows.user

import com.practice.contracts.UserContract
import com.practice.flows.BaseFlow
import com.practice.states.UserState
import net.corda.core.contracts.Command
import net.corda.core.flows.StartableByRPC
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder

@StartableByRPC
class LoginFlow (private val userUID: String,
                 private val jwtToken: String): BaseFlow()
{
    override fun call(): SignedTransaction {
        val transaction = transaction()
        val ptx = verifyAndSign(transaction)
        return recordTransactionWithoutCounterParty(ptx)
    }

    private fun userState(): UserState {
        val userState = userServices().getUserById(userUID).state.data
        return userState.loginUser(jwtToken)
    }

    private fun transaction(): TransactionBuilder {
        val notary = utilityServices().getNotary()
        val userState = userState()
        val builder = TransactionBuilder(notary = notary)

        val loginUserCmd = Command(
                value = UserContract.Commands.Login(),
                signers = userState.participants.map { it.owningKey }
        )

        builder.addCommand(loginUserCmd)
        builder.addOutputState(userState, UserContract.USER_ID)

        return builder
    }
}