package main.daos

import com.amazonaws.services.simpledb.model.ReplaceableAttribute
import framework.models.BaseEntityNamespace

/**
 * Representation of a Token -- used when transfering/sharing tokens via transactions
 *
 * @property id
 * @property amount
 * @property tokenType
 */
class Token(
    val name: String,
    val parent: String? = null,
    val conversion: Double? = null
) : BaseEntityNamespace() {
    override fun toMap(): MutableMap<String, Any?> {
        var map = super.toMap()
        map.put("name", name)
        if(parent != null)
            map["parent"] = parent
        if(conversion != null)
            map["conversion"] = conversion
        return map
    }

    override fun getAttributes(): MutableList<ReplaceableAttribute> {
        var list = super.getAttributes()
        list.add(ReplaceableAttribute("name", name, true))
        if(parent != null)
            list.add(ReplaceableAttribute("parent", parent, true))
        if(conversion != null)
            list.add(ReplaceableAttribute("conversion", conversion.toString(), true))
        return list
    }
}