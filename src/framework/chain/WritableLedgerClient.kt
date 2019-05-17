package framework.chain

import framework.models.BaseObject
import main.daos.ActionType
import main.daos.CryptoKeyPair

interface WritableLedgerClient<T: BaseObject>: LedgerClient<T> {
    // Write a new transaction to the ledger
    // This will get the latest state of the particular transaction type
    // and validate the transaction is correct.
    // NOTE: you should call this super() to construct and validate
    // TODO: move validation into ledger write, add 'db transaction' functionality
    // TODO: remove double validation
    fun write(keyPair: CryptoKeyPair, to: String?, actionType: ActionType, data: BaseObject) {
        val transaction = ledger.constructor.construct(keyPair, to, actionType, data)
        // TODO -- ADD A REDIS LOCK HERE FOR THE keypair
        validator.validate(transaction)
        ledger.write(transaction)
        validator.validate(transaction.id)
    }

    fun update(keyPair: CryptoKeyPair, data: BaseObject) {
        return write(keyPair, keyPair.publicKey, ActionType.UPDATE, data)
    }
}