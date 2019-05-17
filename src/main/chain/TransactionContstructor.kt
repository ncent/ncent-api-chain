package main.chain

import framework.chain.Constructor
import framework.models.BaseObject
import main.daos.ActionType
import main.daos.CryptoKeyPair
import main.daos.Transaction

class TransactionContstructor: Constructor<Transaction> {
    override fun construct(
        keyPair: CryptoKeyPair,
        to: String?,
        actionType: ActionType,
        data: BaseObject
    ): Transaction {
        throw NotImplementedError()
    }

    override fun construct(vararg kvp: Pair<String, String>): String {
        throw NotImplementedError()
    }
}