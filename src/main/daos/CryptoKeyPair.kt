package main.daos

import com.amazonaws.services.simpledb.model.ReplaceableAttribute
import framework.models.BaseObject
import main.helpers.EncryptionHelper

/**
 * Api credentials
 * @property publicKey
 * @property privateKey
 */

// We pass the public key as the ID to make sure we have uniqueness
data class CryptoKeyPair(
    val publicKey: String,
    private val _privateKey: String,
    val keyPairType: String
): BaseObject {
    val privateKey: String
    val _privateKeySalt: String

    init {
        val encryption = EncryptionHelper.encrypt(_privateKey)
        this.privateKey = encryption.first
        _privateKeySalt = encryption.second
    }

    override fun toMap(): MutableMap<String, Any?> {
        return mutableMapOf(
            Pair("publicKey", publicKey),
            Pair("keyPairType", keyPairType)
        )
    }

    override fun getAttributes(): MutableList<ReplaceableAttribute> {
        return mutableListOf(
            ReplaceableAttribute("publicKey", publicKey, true),
            ReplaceableAttribute("_privateKey", _privateKey, true),
            ReplaceableAttribute("_privateKeySalt", _privateKeySalt, true),
            ReplaceableAttribute("keyPairType", keyPairType, true)
        )
    }
}

data class NewCryptoKeyPair(
    val value: CryptoKeyPair,
    val secret: String
)