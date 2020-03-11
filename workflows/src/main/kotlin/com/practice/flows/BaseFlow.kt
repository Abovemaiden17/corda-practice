package com.practice.flows

import co.paralleluniverse.fibers.Suspendable
import com.practice.services.UserServices
import com.practice.services.UtilityServices
import com.practice.utils.*
import net.corda.core.flows.CollectSignaturesFlow
import net.corda.core.flows.FinalityFlow
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.FlowSession
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.ProgressTracker

abstract class BaseFlow : FlowLogic<SignedTransaction>() {
    protected fun utilityServices() = serviceHub.cordaService(UtilityServices::class.java)
    protected fun userServices() = serviceHub.cordaService(UserServices::class.java)

    // Progress Tracker
    override val progressTracker = ProgressTracker(
            CREATING, VERIFYING, SIGNING, NOTARIZING, FINALIZING
    )

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
}