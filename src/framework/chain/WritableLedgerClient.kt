package framework.chain

import framework.models.BaseObject
import main.daos.ActionType
import main.daos.CryptoKeyPair

interface WritableLedgerClient<T: BaseObject>: LedgerClient<T> {
    // Write a new transaction to the ledger
    // This will get the latest state of the particular transaction type
    // and validate the transaction is correct.
    // NOTE: you should call this super() to construct and validate
    fun write(keyPair: CryptoKeyPair, to: String?, actionType: ActionType, data: T) {
        val transaction = ledger.constructor.construct(keyPair, to, actionType, data)
        validator.validate(transaction)
        ledger.write(transaction)
    }
}