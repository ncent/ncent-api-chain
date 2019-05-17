package main.services.user_account

import kotlinserverless.framework.models.Handler
import kotlinserverless.framework.models.InvalidArguments
import main.daos.*
import main.helpers.UserAccountHelper
import java.util.regex.Pattern

/**
 * This service will be used to generate a full User Account
 */
object GenerateUserAccountService {

    private val EMAIL_ADDRESS_PATTERN = Pattern.compile(
    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
    "\\@" +
    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
    "(" +
    "\\." +
    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
    ")+"
    );

    fun execute(email: String, firstname: String, lastname: String) : NewUserAccount {
        val apiCred = UserAccountHelper.generateApiCred()
        val keyPair = UserAccountHelper.generateNewCryptoKeyPair()

        if(!EMAIL_ADDRESS_PATTERN.matcher(email).matches())
            throw InvalidArguments("Must enter a valid email address")

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
