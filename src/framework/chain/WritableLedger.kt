package framework.chain

import main.daos.CryptoKeyPair
import main.daos.Transaction

abstract class WritableLedger<
        T: Transaction,
        V: Validator<T>,
        C: Constructor<T>,
        L: Ledger<T>
>(
    private val validator: V,
    private val constructor: C,
    private val ledger: L
) {
    // Write a new transaction to the ledger
    // This will get the latest state of the particular transaction type
    // and validate the transaction is correct.
    // NOTE: you should call this super() to construct and validate
    fun write(keyPair: CryptoKeyPair, to: String?, vararg kvp: Pair<String, String>): T {
        val transaction = constructor.construct(keyPair, to, *kvp)
        validator.validate(transaction)
        return ledger.write(transaction)
    }
}