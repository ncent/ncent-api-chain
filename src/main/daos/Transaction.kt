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
    val action: Action,
    val amount: Double? = null,
    val previousTransaction: String? = null,
    val metadatas: Metadatas? = null
): BaseEntity() {

    fun getQuery(): String {
        throw NotImplementedError()
    }

    override fun getAttributes(): MutableList<ReplaceableAttribute> {
        var list = super.getAttributes()
        list.add(ReplaceableAttribute("from", from, true))
        list.add(ReplaceableAttribute("to", to, true))
        list.addAll(action.getAttributes())
        if(amount != null)
            list.add(ReplaceableAttribute("amount", amount.toString(), true))
        if(previousTransaction != null)
            list.add(ReplaceableAttribute("previousTransaction", previousTransaction.toString(), true))
        if(metadatas != null)
            list.addAll(metadatas.getAttributes())
        return list
    }

    override fun toMap(): MutableMap<String, Any?> {
        var map = super.toMap()
        map.put("from", from)
        map.put("to", to)
        map.put("action", action.toMap())
        if(amount != null)
            map.put("amount", amount)
        if(previousTransaction != null)
            map.put("previousTransactionId", previousTransaction)
        if(metadatas != null)
            map.putAll(metadatas.toMap())
        return map
    }
}

data class TransactionNamespaceList(val transactions: List<Transaction>)

data class TransactionToShareNamespace(val transaction: Transaction, val shares: Int)

data class ShareTransactionListNamespace(val transactionsToShares: List<TransactionToShareNamespace>)

data class TransactionWithNewUserNamespace(val transactions: List<Transaction>, val newUser: NewUserAccount? = null)

/**
 * Representation of an action taking place and being stored in a transaction
 * @property type This is the action type; ex: transfer, create, share, payout.
 * @property data The data object; ex: a particular token, a particular Challenge
 * @property dataType This is the object type; ex: Token, Challenge.
 */

data class Action(
    val type: ActionType?=null,
    val dataType: String,
    val dataId: String?=null,
    val data: BaseEntityNamespace?=null
): BaseNamespace() {
    override fun toMap(): MutableMap<String, Any?> {
        val map = mutableMapOf<String, Any?>()
        map["dataType"] = dataType
        if(data != null)
            map["data"] = data
        if(type != null)
            map["type"] = type.toString()
        if(dataId != null)
            map["dataId"] = dataId
        return map
    }

    override fun getAttributes(): MutableList<ReplaceableAttribute> {
        var list = mutableListOf(
                ReplaceableAttribute("dataType", dataType, true)
        )
        if(data != null)
            list.addAll(data.getAttributes())
        if(type != null)
            list.add(ReplaceableAttribute("type", type.type, true))
        if(dataId != null)
            list.add(ReplaceableAttribute("dataId", dataId, true))
        return list
    }
}

enum class ActionType(val type: String) {
    TRANSFER("transfer"),
    CREATE("create"),
    SHARE("share"),
    PAYOUT("payout"),
    ACTIVATE("activate"),
    COMPLETE("complete"),
    INVALIDATE("invalidate"),
    EXPIRE("expire"),
    UPDATE("update")
}