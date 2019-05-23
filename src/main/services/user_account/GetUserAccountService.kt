package main.services.user_account

import kotlinserverless.framework.models.Handler
import main.daos.*
import kotlinserverless.framework.models.NotFoundException

object GetUserAccountService {
    fun execute(publicKey: String? = null, email: String? = null, apiKey: String? = null): UserAccount {
        var query = arrayListOf(
            Pair("dataType", UserAccount::class.simpleName!!)
        )
        when {
            apiKey != null -> {
                query.add(Pair("apiKey", apiKey))
            }
            publicKey != null -> {
                query.add(Pair("publicKey", publicKey))
            }
            email != null -> {
                query.add(Pair("email", email))
            }
            else -> {
                throw NotFoundException()
            }
        }
        return Handler.ledgerClient.read(*query.toTypedArray()).action.data as UserAccount
    }
}