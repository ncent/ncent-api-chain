package main.services.user_account

import kotlinserverless.framework.models.Handler
import kotlinserverless.framework.models.UnauthorizedError
import main.daos.*
import main.helpers.EncryptionHelper

/**
 * Validate the accuracy of the passed crypto pub/priv in the UserAccount
 */
object ValidateCryptoKeyPairService {
    fun execute(publicKey: String, privateKey: String) {
        val user = Handler.ledgerClient.read(
            Pair("publicKey", publicKey)
        ).action.data as UserAccount
        val valid = EncryptionHelper.validateEncryption(privateKey,
            user.cryptoKeyPair._privateKeySalt,
            user.cryptoKeyPair.privateKey
        )
        if(!valid)
            throw UnauthorizedError()
    }
}