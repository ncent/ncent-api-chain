package framework.chain

interface Ledger<T> {
    val constructor: Constructor<T>
    fun read(query: () -> T): List<T>
    fun write(value: T)
}