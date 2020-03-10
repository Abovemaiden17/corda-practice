package com.practice.functions

import co.paralleluniverse.fibers.Suspendable
import com.practice.states.UserState
import com.practice.utils.*
import net.corda.core.CordaException
import net.corda.core.contracts.StateAndRef
import net.corda.core.flows.CollectSignaturesFlow
import net.corda.core.flows.FinalityFlow
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.FlowSession
import net.corda.core.identity.CordaX500Name
import net.corda.core.identity.Party
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.ProgressTracker

abstract class UserFunctions : FlowLogic<StateAndRef<UserState>>() {
    // Progress Tracker
    override val progressTracker = ProgressTracker(
            CREATING, VERIFYING, SIGNING, NOTARIZING, FINALIZING
    )

    // Get the first notary of the network map
    fun notary() = serviceHub.networkMapCache.notaryIdentities.first()

    // Verification and Signing of Transaction
    fun verifyAndSign(transaction: TransactionBuilder): SignedTransaction {
        transaction.verify(serviceHub)
        return serviceHub.signInitialTransaction(transaction)
    }

    // Collecting signatures of parties involved in the transaction
    @Suspendable
    fun collectSignature(
            transaction: SignedTransaction,
            sessions: List<FlowSession>
    ): SignedTransaction = subFlow(CollectSignaturesFlow(transaction, sessions))

    @Suspendable
    fun recordTransactionWithoutCounterParty(transaction: SignedTransaction) : SignedTransaction {
        return subFlow(FinalityFlow(transaction, emptyList()))
    }

    @Suspendable
    fun recordTransactionWithCounterParty(transaction: SignedTransaction, sessions: List<FlowSession>): SignedTransaction {
        return subFlow(FinalityFlow(transaction, sessions))
    }

    // Conversion of string to a Party
    fun stringToParty(name: String): Party
    {
        return serviceHub.identityService.wellKnownPartyFromX500Name(CordaX500Name.parse(name))
                ?: throw CordaException("No match found for $name")
    }
}