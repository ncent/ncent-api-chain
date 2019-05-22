package main.chain

import framework.chain.Ledger
import framework.chain.Validator
import main.daos.Transaction

class TransactionValidator<T: Transaction>(
    override val ledger: Ledger<T>
): Validator<T> {
    override fun validate(id: String) {
        // TODO
        throw NotImplementedError()
    }
    override fun validate(obj: T) {
        // TODO
        throw NotImplementedError()
    }
}