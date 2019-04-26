package main.daos

import com.amazonaws.services.simpledb.model.ReplaceableAttribute
import framework.models.BaseEntityNamespace
import main.helpers.EncryptionHelper

/**
 * Api credentials
 * @property apiKey
 * @property secretKey
 */
data class ApiCred(
    val apiKey: String,
    private val _secretKey: String
): BaseEntityNamespace() {
    override val className = "api_cred"

    val secretKey: String
    private val _secretKeySalt: String

    init {
        val encryption = EncryptionHelper.encrypt(_secretKey)
        this.secretKey = encryption.first
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
