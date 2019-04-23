package main.daos

import com.amazonaws.services.simpledb.model.ReplaceableAttribute
import framework.models.*

/**
 * Transaction represents the data that records any changes related to any
 * object. This will be stored in an immutable way to represent how it would
 * be stored on a blockchain. This can also track off-chain transactions; which this
 * model can easily represent, however will not be stored.
 *
 * @property id
 * @property from Address that triggers this transaction
 * @property to Optionally an address this transaction is sending to
 * @property action The action taking place
 * @property previousTransaction Optionally the previous transaction related to this transaction
 * @property metadatas Optionally can be used to keep track of additional data. (ex: max shares)
 * example being: challenge sharing (providence chain)
 */
class Transaction(
    val from: String,
    val to: String,
    val action: ActionNamespace,
    val previousTransaction: String? = null,
    val metadatas: List<MetadatasNamespace>? = null
): BaseEntity() {

    fun getQuery(): String {
        throw NotImplementedError()
    }

    override fun getAttributes(): MutableList<ReplaceableAttribute> {
        var list = super.getAttributes()
        list.add(ReplaceableAttribute("from", from, true))
        list.add(ReplaceableAttribute("to", to, true))
        list.addAll(action.getAttributes())
        if(metadatas != null)
            list.addAll(metadatas.map { it.getAttributes() }.flatten())
        return list
    }

    override fun toMap(): MutableMap<String, Any?> {
        var map = super.toMap()
        map.put("from", from)
        map.put("to", to)
        map.put("action", action.toMap())
        if(previousTransaction != null)
            map.put("previousTransactionId", previousTransaction)
        if(metadatas != null)
            map.put("metadatas", metadatas.map { it.toMap() })
        return map
    }
}

data class TransactionNamespace(val from: String?=null, val to: String?=null, val action: ActionNamespace?=null, val previousTransaction: Int?=null, val metadatas: Array<MetadatasNamespace>? = null)

data class TransactionNamespaceList(val transactions: List<TransactionNamespace>)

data class TransactionToShareNamespace(val transactions: TransactionNamespace, val shares: Int)

data class ShareTransactionListNamespace(val transactionsToShares: List<TransactionToShareNamespace>)

data class TransactionWithNewUserNamespace(val transactions: List<TransactionNamespace>, val newUser: NewUserAccountNamespace? = null)