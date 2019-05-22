package framework.chain

import main.daos.Transaction

interface LedgerClient<T: Transaction> {
    val validator: Validator<T>
    val ledger: Ledger<T>
}