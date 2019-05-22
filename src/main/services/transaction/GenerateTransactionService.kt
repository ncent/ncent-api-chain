package main.services.transaction

import framework.models.BaseEntityNamespace
import kotlinserverless.framework.models.Handler
import main.daos.*

/**
 * Generate a transaction if it is valid
 */
object GenerateTransactionService {
    fun execute(
        keyPair: CryptoKeyPair,
        to: String,
        type: ActionType,
        dataType: String,
        dataId: String?=null,
        data: BaseEntityNamespace?=null,
        amount: Double? = null,
        previousTransaction: String? = null,
        metadatas: Metadatas? = null
    ) : Unit {
        // TODO add to DB
        // TODO add a redis lock
        val transaction = Transaction(
            keyPair.publicKey,
            to,
            Action(type, dataType, dataId, data),
            amount,
            previousTransaction,
            metadatas
        )
        Handler.ledgerClient.write(
            keyPair,
            transaction
        )
    }
}