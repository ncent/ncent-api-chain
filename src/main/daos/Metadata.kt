package main.daos

import com.amazonaws.services.simpledb.model.ReplaceableAttribute
import framework.models.BaseNamespace

/**
 * Metadata will be a key-value store
 *
 * @property id
 * @property key
 * @property value
 */

data class MetadatasNamespace(
    val key: String,
    val value: String
): BaseNamespace {
    override fun toMap(): MutableMap<String, Any?> {
        val map = mutableMapOf<String, Any?>()
        map["key"] = key
        map["value"] = value
        return map
    }

    override fun getAttributes(): MutableList<ReplaceableAttribute> {
        return mutableListOf(
            ReplaceableAttribute("key", key, true),
            ReplaceableAttribute("value", value, true)
        )
    }
}