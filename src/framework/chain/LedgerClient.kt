package framework.chain

interface LedgerClient<T> {
    val validator: Validator<T>
    val constructor: Constructor<T>
    val ledger: Ledger<T>
}