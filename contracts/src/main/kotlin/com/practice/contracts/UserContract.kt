package com.practice.contracts

import net.corda.core.contracts.Contract
import net.corda.core.contracts.TypeOnlyCommandData
import net.corda.core.contracts.requireSingleCommand
import net.corda.core.transactions.LedgerTransaction

class UserContract : Contract
{
    companion object {
        const val USER_ID = "com.practice.contracts.UserContract"
    }

    override fun verify(tx: LedgerTransaction) {
        val command = tx.commands.requireSingleCommand<Commands>()

        when (command.value) {
            is Commands.Register -> {
                //TODO
            }
            is Commands.Change -> {
                //TODO
            }
            is Commands.Login -> {
                //TODO
            }
        }
    }

    sealed class Commands : TypeOnlyCommandData() {
        class Register : Commands()
        class Change : Commands()
        class Login : Commands()
    }
}