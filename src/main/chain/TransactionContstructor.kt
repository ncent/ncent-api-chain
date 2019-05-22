package main.chain

import framework.chain.Constructor
import main.daos.Transaction

class TransactionContstructor: Constructor<Transaction> {
    override fun construct(vararg kvp: Pair<String, String>): String {
        throw NotImplementedError()
    }
}