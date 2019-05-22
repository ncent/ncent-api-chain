package main.services.user_account

import kotlinserverless.framework.models.Handler
import main.daos.*
import org.joda.time.DateTime

/**
 * This service will be used to generate a full User Account
 */
object DeleteUserAccountService {
    fun execute(user: UserAccount) {
        val userAccount = UserAccount(
            User(
                "DELETED_${DateTime.now()}_${user.userMetadata.email}",
                user.userMetadata.firstname,
                user.userMetadata.lastname
            ),
            user.cryptoKeyPair,
            user.apiCred
        )
        Handler.ledgerClient.delete(
            user.cryptoKeyPair,
            userAccount
        )
    }
}
