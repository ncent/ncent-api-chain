package main.services.user_account

import kotlinserverless.framework.models.Handler
import main.daos.*
import main.helpers.UserAccountHelper

/**
 * This service will be used to reset a user account's ApiCreds
 */
object ResetApiCredsService {
     fun execute(userAccount: UserAccount) : ApiCred {
         val apiCred = UserAccountHelper.generateApiCred()

         Handler.ledgerClient.update(
             userAccount.cryptoKeyPair,
             UserAccount(
                 userAccount.userMetadata,
                 userAccount.cryptoKeyPair,
                 apiCred
             )
         )

         return apiCred
    }
}
