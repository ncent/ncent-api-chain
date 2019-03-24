package framework.chain

import main.daos.CryptoKeyPair

interface WritableLedgerClient<T>: LedgerClient<T> {
    // Write a new transaction to the ledger
    // This will get the latest state of the particular transaction type
    // and validate the transaction is correct.
    // NOTE: you should call this super() to construct and validate
    fun write(keyPair: CryptoKeyPair, to: String?, vararg kvp: Pair<String, String>) {
        val transaction = ledger.constructor.construct(keyPair, to, *kvp)
        validator.validate(transaction)
        ledger.write(transaction)
    }
}