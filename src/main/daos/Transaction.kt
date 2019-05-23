package main.daos

import com.amazonaws.services.simpledb.model.ReplaceableAttribute
import framework.models.*
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

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
 * @property metadatas Optionally can be used to keep track of additional data. (ex: max shares)
 * example being: challenge sharing (providence chain)
 */
class Transaction(
    val from: String,
    val to: String,
    val action: Action,
    val amount: Double? = null,
    val metadatas: Metadatas? = null
): BaseObject {
    val createdAt = DateTime.now(DateTimeZone.UTC)

    constructor(from: String, to: String, type: ActionType, data: BaseObject) :
        this(from, to, Action(type, data::class.simpleName!!, data))

    override fun getAttributes(): MutableList<ReplaceableAttribute> {
        var list = super.getAttributes()
        list.add(ReplaceableAttribute("from", from, true))
        list.add(ReplaceableAttribute("to", to, true))
        list.add(ReplaceableAttribute("createdAt", createdAt.toString(), true))
        list.addAll(action.getAttributes())
        if(amount != null)
            list.add(ReplaceableAttribute("amount", amount.toString(), true))
        if(metadatas != null)
            list.addAll(metadatas.getAttributes())
        return list
    }

    override fun toMap(): MutableMap<String, Any?> {
        var map = super.toMap()
        map.put("from", from)
        map.put("to", to)
        map.put("action", action.toMap())
        map.put("createdAt", createdAt.toString())
        if(amount != null)
            map.put("amount", amount)
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
    val type: ActionType,
    val dataType: String,
    val data: BaseObject?=null
): BaseObject {
    override fun toMap(): MutableMap<String, Any?> {
        val map = mutableMapOf<String, Any?>()
        map["type"] = type.toString()
        map["dataType"] = dataType
        if(data != null)
            map["data"] = data
        return map
    }

    override fun getAttributes(): MutableList<ReplaceableAttribute> {
        var list = mutableListOf(
            ReplaceableAttribute("dataType", dataType, true),
            ReplaceableAttribute("type", type.type, true)
        )
        if(data != null)
            list.addAll(data.getAttributes())
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