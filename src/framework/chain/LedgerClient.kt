package framework.chain

import framework.models.BaseObject

interface LedgerClient<T: BaseObject> {
    val validator: Validator<T>
    val ledger: Ledger<T>
}