package main.helpers

import framework.chain.Constructor
import main.daos.CryptoKeyPair
import main.daos.Transaction

class TransactionConstructor<T: Transaction>: Constructor<T> {
    override fun construct(keyPair: CryptoKeyPair, to: String?, vararg kvp: Pair<String, String>): T {
        throw NotImplementedError()
    }
}