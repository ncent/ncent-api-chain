package main.services.user_account

import kotlinserverless.framework.models.UnauthorizedError
import main.daos.UserAccount
import main.helpers.EncryptionHelper

/**
 * Validate the accuracy of the passed ApiKey and Secret key in the UserAccount
 */
object ValidateApiKeyService {
    fun execute(caller: UserAccount, secretKey: String) {
        val valid = EncryptionHelper.validateEncryption(secretKey,
            caller.apiCred._secretKeySalt,
            caller.apiCred.secretKey
        )
        if(!valid)
            throw UnauthorizedError()
    }
}