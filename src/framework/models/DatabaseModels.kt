package framework.models

import com.amazonaws.services.simpledb.model.ReplaceableAttribute

interface BaseObject {
    fun getAttributes(): MutableList<ReplaceableAttribute> {
        return mutableListOf()
    }

    fun toMap(): MutableMap<String, Any?> {
        return mutableMapOf()
    }
}
