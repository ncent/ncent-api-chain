package framework.chain

import framework.models.BaseObject

interface Ledger<T: BaseObject> {
    val constructor: Constructor<T>
    fun read(query: String): List<T>
    fun write(value: T)
}