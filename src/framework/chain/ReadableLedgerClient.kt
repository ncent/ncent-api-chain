package framework.chain

interface ReadableLedgerClient<T>: LedgerClient<T> {

    // Read the latest state for a particular transaction type
    // the type is determined by the set of key/value pairs
    // ex: get the latest balance for a particular asset type and particular state
    fun read(address: String, vararg kvp: Pair<String, String>): T

    // Read the history for a particular transaction type
    // ex: get all transactions to/from a particular address for a particular asset type and state
    fun readAll(address: String, vararg kvp: Pair<String, String>): List<T>
}