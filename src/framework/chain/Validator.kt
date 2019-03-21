package framework.chain

import main.daos.Transaction

interface Validator<T: Transaction> {

    // Used to validate a particular transaction type
    // Each transaction type should have it's own validator
    fun validate(obj: T) {
        throw NotImplementedError()
    }
}