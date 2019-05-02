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

data class Metadatas(
    val metadatas: Map<String, String>,
    val metadatasClassName: String
): BaseNamespace(metadatasClassName) {

    override fun toMap(): MutableMap<String, Any?> {
        val map = super.toMap()
        metadatas.forEach { map[it.key] = it.value }
        return map
    }

    override fun getAttributes(): MutableList<ReplaceableAttribute> {
        val list = super.getAttributes()
        metadatas.forEach { list.add(ReplaceableAttribute(it.key, it.value, true)) }
        return list
    }
}