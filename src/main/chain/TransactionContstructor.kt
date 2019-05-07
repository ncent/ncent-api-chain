package main.chain

import framework.chain.Constructor
import main.daos.ActionType
import main.daos.CryptoKeyPair
import main.daos.Transaction

class TransactionContstructor: Constructor<Transaction> {
    override fun construct(keyPair: CryptoKeyPair, to: String?, actionType: ActionType, data: Transaction): Transaction {
        throw NotImplementedError()
    }
}