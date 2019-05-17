package main.services.user_account

import kotlinserverless.framework.models.Handler
import main.daos.*
import main.helpers.UserAccountHelper

/**
 * This service is used to reset a user's public / private keypair.
 */
object ResetCryptoKeyPairService {
    fun execute(userAccount: UserAccount) : NewCryptoKeyPair {
        val keyPair = UserAccountHelper.generateNewCryptoKeyPair()

        Handler.ledgerClient.update(
            userAccount.cryptoKeyPair,
            UserAccount(
                userAccount.userMetadata,
                keyPair.value,
                userAccount.apiCred
            )
        )

        return keyPair
    }
}
