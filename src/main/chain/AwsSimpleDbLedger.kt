package main.chain

import framework.chain.*
import main.daos.Transaction

class AwsSimpleDbLedger<T: Transaction>: Ledger<T> {
    override fun read(query: () -> T): T {
        throw NotImplementedError()
    }

    override fun write(value: T): T {
        throw NotImplementedError()
    }
}