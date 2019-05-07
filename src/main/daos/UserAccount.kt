package main.daos

import com.amazonaws.services.simpledb.model.ReplaceableAttribute
import framework.models.BaseEntityNamespace
import framework.models.BaseObject
import main.helpers.EncryptionHelper

/**
 * User Accounts will be used exclusively by the API in order to manage
 * user usage of the API. This will handle storage of the user information,
 * their wallet information, and the api information. Services will include:
 * generation, validation, and throttling.
 * @property id
 * @property userMetadata UserMetadata
 * @property cryptoKeyPair CryptoKeyPair
 * @property apiCreds ApiCreds
 */
class UserAccount(
    val userMetadata: User,
    val cryptoKeyPair: CryptoKeyPair,
    val apiCred: ApiCred
) : BaseEntityNamespace() {

    override fun getAttributes(): MutableList<ReplaceableAttribute> {
        var list = super.getAttributes()
        list.addAll(userMetadata.getAttributes())
        list.addAll(cryptoKeyPair.getAttributes())
        list.addAll(apiCred.getAttributes())
        return list
    }

    override fun toMap(): MutableMap<String, Any?> {
        var map = super.toMap()
        map.put("userMetadata", userMetadata.toMap())
        map.put("cryptoKeyPair", cryptoKeyPair.toMap())
        map.put("apiCred", apiCred.toMap())
        return map
    }
}

data class NewUserAccount(
    val value: UserAccount,
    val privateKey: String,
    val secretKey: String
)

/**
 * Api credentials
 * @property apiKey
 * @property secretKey
 */
data class ApiCred(
    val apiKey: String,
    private val _secretKey: String
): BaseEntityNamespace() {
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

/**
 * Basic fields that a User needs
 * @property id User id
 * @property email User email
 * @property firstname User first name
 * @property lastname User last name
 * @property metadata Metadata used to track additional information about the user
 */
data class User(
    val email: String,
    val firstname: String,
    val lastname: String,
    val metadatas: Metadatas? = null
): BaseEntityNamespace() {
    override fun toMap(): MutableMap<String, Any?> {
        var map = super.toMap()
        map.put("email", email)
        map.put("firstname", firstname)
        map.put("lastname", lastname)
        if(metadatas != null)
            map.putAll(metadatas.toMap())
        return map
    }

    override fun getAttributes(): MutableList<ReplaceableAttribute> {
        var list = super.getAttributes()
        list.add(ReplaceableAttribute("email", email, true))
        list.add(ReplaceableAttribute("firstname", firstname, true))
        list.add(ReplaceableAttribute("lastname", lastname, true))
        if(metadatas != null)
            list.addAll(metadatas.getAttributes())
        return list
    }
}

