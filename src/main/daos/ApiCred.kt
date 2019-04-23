package main.daos

import com.amazonaws.services.simpledb.model.ReplaceableAttribute
import framework.models.BaseNamespace
import main.helpers.EncryptionHelper

/**
 * Api credentials
 * @property apiKey
 * @property secretKey
 */
class ApiCredNamespace(
    val apiKey: String,
    var _secretKey: String,
    var _secretKeySalt: String = ""
): BaseNamespace {
    var secretKey : String
        get() = _secretKey
        set(value) {
            val encryption = EncryptionHelper.encrypt(value)
            _secretKey = encryption.first
            _secretKeySalt = encryption.second
        }

    override fun toMap(): MutableMap<String, Any?> {
        return mutableMapOf(Pair("apiKey", apiKey))
    }

    override fun getAttributes(): MutableList<ReplaceableAttribute> {
        return mutableListOf(
            ReplaceableAttribute("apiKey", apiKey, true),
            ReplaceableAttribute("_secretKey", _secretKey, true),
            ReplaceableAttribute("_secretKeySalt", _secretKeySalt, true)
        )
    }
}
