package main.controllers

import framework.services.DaoService
import kotlinserverless.framework.controllers.RestController
import kotlinserverless.framework.controllers.DefaultController
import kotlinserverless.framework.models.NotFoundException
import kotlinserverless.framework.services.SOAResult
import kotlinserverless.framework.models.Request
import kotlinserverless.framework.services.SOAResultType
import main.daos.*
import main.services.user_account.ValidateApiKeyService
import main.services.challenge.*
import main.helpers.JsonHelper

class ChallengeController: DefaultController<Challenge>(), RestController<Challenge, UserAccount> {
    override fun create(user: UserAccount, params: Map<String, String>): SOAResult<*> {
        return DaoService.execute {
            ValidateApiKeyService.execute(user, params["secretKey"] as String)
            val challengeNamespace = JsonHelper.parse<ChallengeNamespace>(params["challengeNamespace"]!!)

            val generateChallengeResult = GenerateChallengeService.execute(user, challengeNamespace)
            DaoService.throwOrReturn(generateChallengeResult.result, generateChallengeResult.message)
            return@execute generateChallengeResult.data!!
        }
    }

    @Throws(NotFoundException::class)
    override fun findOne(user: UserAccount, id: Int): SOAResult<Challenge> {
        val findChallengeResult = DaoService.execute {
            Challenge.findById(id)
        }

        if (findChallengeResult.result != SOAResultType.SUCCESS || findChallengeResult.data == null) {
            throw NotFoundException("Challenge not found")
        }

        return SOAResult(findChallengeResult.result, findChallengeResult.message, findChallengeResult.data!!)
    }

    fun complete(user: UserAccount, request: Request): SOAResult<Challenge> {
        throw NotImplementedError()
    }

    fun share(user: UserAccount, request: Request): SOAResult<TransactionWithNewUser> {
        return DaoService.execute {
            ValidateApiKeyService.execute(user, request.input["secretKey"] as String)

            val challengeId = request.input["challengeId"] as Int
            val challenge = Challenge.findById(challengeId)!!
            val publicKeyToShareWith = request.input["publicKeyToShareWith"] as String?
            val shares = request.input["shares"] as Int
            val expiration = request.input["expiration"] as String?
            val emailToShareWith = request.input["emailToShareWith"] as String?

            val shareChallengeResult = ShareChallengeService.execute(user, challenge, shares, publicKeyToShareWith, emailToShareWith, expiration)
            DaoService.throwOrReturn(shareChallengeResult.result, shareChallengeResult.message)
            return@execute shareChallengeResult.data!!
        }
    }
}