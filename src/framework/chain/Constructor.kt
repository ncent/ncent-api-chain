package framework.chain

import main.daos.CryptoKeyPair

interface Constructor<T> {
    // Used to construct a new transaction
    // Each transaction type should have it's own constructor
    fun construct(keyPair: CryptoKeyPair, to: String?, vararg kvp: Pair<String, String>): T
}