package main.services.user_account

import main.daos.*

/**
 * This service is used reset a user's API key and crypto key pair.
 */
object ResetUserAccount {
    fun execute(userAccount: UserAccount) : NewUserAccount {
        // Reset the user's API keys
        val updatedApiKeys = ResetApiCredsService.execute(userAccount)

        // Reset the user's crypto keypair
        val updatedCryptoKeyPair = ResetCryptoKeyPairService.execute(userAccount)

        return NewUserAccount(
            userAccount,
            updatedCryptoKeyPair.secret,
            updatedApiKeys.secretKey
        )
    }
}
