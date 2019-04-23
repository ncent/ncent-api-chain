package main.daos

import com.amazonaws.services.simpledb.model.ReplaceableAttribute
import framework.models.BaseNamespace

/**
 * Representation of an action taking place and being stored in a transaction
 * @property type This is the action type; ex: transfer, create, share, payout.
 * @property data The data object; ex: a particular token, a particular Challenge
 * @property dataType This is the object type; ex: Token, Challenge.
 */

data class ActionNamespace(
    val type: ActionType?=null,
    val dataType: String,
    val dataId: String?=null,
    val data: BaseNamespace?=null
): BaseNamespace {
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