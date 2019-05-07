package main.chain

import framework.chain.Validator
import framework.models.BaseEntity
import framework.models.BaseObject
import main.daos.Transaction

class TransactionValidator<T: BaseObject>: Validator<T> {
    override fun validate(obj: T) {
        throw NotImplementedError()
    }
}