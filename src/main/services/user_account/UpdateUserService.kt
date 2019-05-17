package main.services.user_account

import kotlinserverless.framework.models.Handler
import main.daos.*

/**
 * Update a users metadata
 * Make sure to verify email when using this service if the email is being updated!!
 */
object UpdateUserService {
    fun execute(caller: UserAccount, userMetadata: User) {
        Handler.ledgerClient.update(
            caller.cryptoKeyPair,
            UserAccount(
                userMetadata,
                caller.cryptoKeyPair,
                caller.apiCred
            )
        )
    }
}