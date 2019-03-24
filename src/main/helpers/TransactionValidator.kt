package main.helpers

import framework.chain.Validator
import main.daos.Transaction

class TransactionValidator<T: Transaction>: Validator<T> {
    override fun validate(obj: T) {
        throw NotImplementedError()
    }
}