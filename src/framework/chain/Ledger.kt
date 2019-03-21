package framework.chain

interface Ledger<T> {
    fun read(query: () -> T): T
    fun write(value: T): T
}