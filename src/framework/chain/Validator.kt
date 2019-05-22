package framework.chain

import main.daos.Transaction

interface Validator<T: Transaction> {
    val ledger: Ledger<T>
    // Used to validate a particular transaction type
    // Each transaction type should have it's own validator
    fun validate(obj: T)

    fun validate(id: String)
}