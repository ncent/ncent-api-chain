package framework.chain

import main.daos.CryptoKeyPair
import main.daos.Transaction

interface Constructor<T: Transaction> {
    // Used to construct a new transaction
    // Each transaction type should have it's own constructor
    fun construct(keyPair: CryptoKeyPair, to: String?, vararg kvp: Pair<String, String>): T {
        throw NotImplementedError()
    }
}