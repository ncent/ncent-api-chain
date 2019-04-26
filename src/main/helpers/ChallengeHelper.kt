package main.helpers

import kotlinserverless.framework.models.Handler
import main.daos.*
import main.services.challenge.GetChallengesService
import main.services.transaction.GetTransactionsService
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

object ChallengeHelper {
    fun findChallengeByTx(challengeTx: String?): Challenge {
        if (challengeTx == null)
            throw InternalError()

        return Handler.ledgerClient.read(challengeTx).action.data!! as Challenge
    }

    fun getChallenges(user: UserAccount): ChallengeToUnsharedTransactionNamespace {
        return GetChallengesService.execute(user).data!!
    }

    val stateChangeActionTypes = setOf(ActionType.CREATE, ActionType.ACTIVATE, ActionType.COMPLETE, ActionType.INVALIDATE, ActionType.EXPIRE)

    fun canTransitionState(fromState: ActionType, toState: ActionType, expiry: DateTime?): Boolean {
        val result =  when(fromState) {
            ActionType.COMPLETE -> {
                false
            }
            ActionType.CREATE -> {
                return arrayListOf(
                        ActionType.ACTIVATE,
                        ActionType.EXPIRE,
                        ActionType.INVALIDATE
                ).contains(toState)
            }
            ActionType.EXPIRE -> {
                false
            }
            ActionType.INVALIDATE -> {
                return arrayListOf(
                        ActionType.ACTIVATE,
                        ActionType.EXPIRE
                ).contains(toState)
            }
            ActionType.ACTIVATE -> {
                return arrayListOf(
                        ActionType.COMPLETE,
                        ActionType.EXPIRE,
                        ActionType.INVALIDATE
                ).contains(toState)
            }
            else -> throw Exception("Could not find challenge state to move to for ${fromState.type}")
        }
        return if(result && toState == ActionType.EXPIRE && expiry != null) {
            result && shouldExpire(expiry)
        } else {
            result
        }
    }

    fun shouldExpire(expiry: DateTime): Boolean {
        return expiry < DateTime.now(DateTimeZone.UTC)
    }

    private fun getTransactions(challengeId: String, userPublicKey: String): List<Transaction>? {
        return GetTransactionsService.execute(
                null,
                userPublicKey,
                null,
                ActionNamespace(null, challengeId, "Challenge")
        ).data?.transactions
    }

    fun getLastStateChangeTransaction(challengeId: String, userPublicKey: String): Transaction? {
        getTransactions(challengeId, userPublicKey)?.forEach {
            if(stateChangeActionTypes.contains(it.action.type))
                return it
        }
        return null
    }
}