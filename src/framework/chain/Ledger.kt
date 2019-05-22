package framework.chain

import main.daos.Transaction

interface Ledger<T: Transaction> {
    val constructor: Constructor<T>
    fun read(query: String): List<T>
    fun write(value: T)
}