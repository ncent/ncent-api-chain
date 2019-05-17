package framework.chain

import framework.models.BaseObject

interface Validator<T: BaseObject> {
    val ledger: Ledger<T>
    // Used to validate a particular transaction type
    // Each transaction type should have it's own validator
    fun validate(obj: T)

    fun validate(id: String)
}