package framework.chain

import framework.models.BaseEntityNamespace
import main.daos.ActionType
import main.daos.CryptoKeyPair
import main.daos.Transaction

interface WritableLedgerClient<T: Transaction>: LedgerClient<T> {
    // Write a new transaction to the ledger
    // This will get the latest state of the particular transaction type
    // and validate the transaction is correct.
    // NOTE: you should call this super() to construct and validate
    // TODO: move validation into ledger write, add 'db transaction' functionality
    // TODO: remove double validation
    fun write(keyPair: CryptoKeyPair, transaction: T) {
        // TODO -- ADD A REDIS LOCK HERE FOR THE keypair
        // TODO -- fix whatever id is being used in the data
        validator.validate(transaction)
        ledger.write(transaction)
        validator.validate(transaction.id)
    }

    fun write(keyPair: CryptoKeyPair, to: String, type: ActionType, data: BaseEntityNamespace) {
        write(keyPair, Transaction(
            keyPair.publicKey,
            to,
            type,
            data
        ) as T)
    }

    fun create(keyPair: CryptoKeyPair, data: BaseEntityNamespace) {
        write(keyPair, keyPair.publicKey, ActionType.CREATE, data)
    }

    fun update(keyPair: CryptoKeyPair, data: BaseEntityNamespace) {
        write(keyPair, keyPair.publicKey, ActionType.UPDATE, data)
    }

    fun delete(keyPair: CryptoKeyPair, data: BaseEntityNamespace) {
        write(keyPair, "DELETED", ActionType.UPDATE, data)
    }
}