package main.services.transaction

import kotlinserverless.framework.models.Handler
import main.daos.Transaction

/**
 * Generate a transaction if it is valid
 */
object GenerateTransactionService {
    fun execute(transaction: Transaction) : Transaction {
        // TODO add to DB
        Handler.ledgerClient.
        Handler.log(null, "GenerateTransactionService: ${transaction.toMap()}")
        throw NotImplementedError("${transaction.toMap()}")
    }
}