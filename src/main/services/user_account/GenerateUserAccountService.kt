package main.services.user_account

import kotlinserverless.framework.models.Handler
import main.daos.*
import main.helpers.UserAccountHelper

/**
 * This service will be used to generate a full User Account
 */
object GenerateUserAccountService {
    fun execute(email: String, firstname: String, lastname: String) : NewUserAccount {
        val apiCred = UserAccountHelper.generateApiCred()
        val keyPair = UserAccountHelper.generateNewCryptoKeyPair()

        val userAccount = UserAccount(
            User(email, firstname, lastname),
            keyPair.value,
            apiCred
        )

        Handler.ledgerClient.write(
            keyPair.value,
            keyPair.value.publicKey,
            ActionType.CREATE,
            userAccount
        )

        return NewUserAccount(
            userAccount,
            keyPair.secret,
            apiCred.secretKey
        )
    }
}
