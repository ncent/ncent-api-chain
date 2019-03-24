package framework.chain

interface LedgerClient<T> {
    val validator: Validator<T>
    val ledger: Ledger<T>
}