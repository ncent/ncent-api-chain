package main.daos

import main.services.transaction.GetTransactionsService
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

/**
 * Used to represent challenges and store pointers to models that
 * store the state for a challenge over the lifecycle of that challenge
 *
 * @property id
 * @property parentChallenge the parent of this challenge, typically this challenge must be completed before the parent
 * @property challengeSettings ChallengeSettings
 * @property subChallenges sub challenges
 * @property completionCriterias CompletionCriterias
 * @property distributionFeeReward the distribution fees and the pool. this is the
 * pool that will be drawn on if anybody 'opts-out' of attempting to help.
 */
class Challenge(id: EntityID<Int>) : BaseIntEntity(id, Challenges) {
    companion object : BaseIntEntityClass<Challenge>(Challenges)

    var parentChallenge by Challenge optionalReferencedOn Challenges.parentChallenge
    var challengeSettings by ChallengeSetting referencedOn Challenges.challengeSettings
    var subChallenges by SubChallenge via ChallengeToSubChallenges
    var completionCriterias by CompletionCriteria referencedOn Challenges.completionCriteria
    var cryptoKeyPair by CryptoKeyPair referencedOn Challenges.cryptoKeyPair
    var distributionFeeReward by Reward referencedOn Challenges.distributionFeeReward

    override fun toMap(): MutableMap<String, Any?> {
        val map = super.toMap()
        map["parentChallenge"] = parentChallenge?.idValue?.toString()
        map["challengeSettings"] = challengeSettings.toMap()
        map["subChallenges"] = subChallenges.map { it.toMap() }
        map["completionCriteria"] = completionCriterias.toMap()
        map["cryptoKeyPair"] = cryptoKeyPair.toMap()
        map["distributionFeeReward"] = distributionFeeReward.toMap()
        return map
    }

    fun canTransitionState(fromState: ActionType, toState: ActionType): Boolean {
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
        return if(result && toState == ActionType.EXPIRE) {
            result && shouldExpire()
        } else {
            result
        }
    }

    fun shouldExpire(): Boolean {
        return challengeSettings.expiration < DateTime.now(DateTimeZone.UTC)
    }

    val stateChangeActionTypes = setOf(ActionType.CREATE, ActionType.ACTIVATE, ActionType.COMPLETE, ActionType.INVALIDATE, ActionType.EXPIRE)

    private fun getTransactions(): List<Transaction>? {
        return GetTransactionsService.execute(
            null,
            cryptoKeyPair.publicKey,
            null,
            ActionNamespace(null, idValue, "Challenge")
        ).data?.transactions
    }

    fun getLastStateChangeTransaction(): Transaction? {
        getTransactions()?.forEach {
            if(stateChangeActionTypes.contains(it.action.type))
                return it
        }
        return null
    }
}

data class ChallengeToUnsharedTransactionNamespace(val challenge: ChallengeNamespace, val shareTransactionList: ShareTransactionListNamespace)

data class UserAccountChallengerNamespace(val challenger: UserAccountNamespace, val receivers: List<UserAccountChallengerNamespace>)

data class ChallengeToUnsharedTransactionsNamespaceList(val challengeToUnsharedTransactions: List<ChallengeToUnsharedTransactionNamespace>)

class SubChallenge(id: EntityID<Int>) : BaseIntEntity(id, SubChallenges) {
    companion object : BaseIntEntityClass<SubChallenge>(SubChallenges)

    var subChallenge by SubChallenges.subChallenge
    var type by SubChallenges.type

    override fun toMap(): MutableMap<String, Any?> {
        var map = super.toMap()
        map.put("subChallengeId", idValue)
        map.put("type", type.toString())
        return map
    }
}

data class SubChallengeNamespace(val subChallengeId: Int?, val type: String?)

data class ChallengeNamespace(
    val challengeSettings: ChallengeSettingNamespace,
    val completionCriteria: CompletionCriteriaNamespace,
    val distributionFeeReward: RewardNamespace,
    val subChallenges: List<SubChallengeNamespace>? = null,
    val parentChallenge: String? = null
    )

enum class SubChallengeType(val type: String) {
    SYNC("sync"), ASYNC("async")
}

/**
 * This data type will be used to generate a map for transaction metadata
 * when generating a challenge based transaction
 * This will store information about that particular transaction ex:
 * when receiving a shared challenge the transaction data will hold how many shares you have available to make
 */
data class ChallengeMetadata(
    val challengeId: Int,
    val offChain: Boolean,
    val shareExpiration: String,
    val maxShares: Int?
) {
    fun getChallengeMetadataNamespaces(): List<MetadatasNamespace> {
        var challengeMetadatas = mutableListOf<MetadatasNamespace>()
        challengeMetadatas.add(MetadatasNamespace("challengeId", challengeId.toString()))
        challengeMetadatas.add(MetadatasNamespace("offChain", offChain.toString()))
        challengeMetadatas.add(MetadatasNamespace("shareExpiration", shareExpiration))
        if(maxShares != null)
            challengeMetadatas.add(MetadatasNamespace("maxShares", maxShares.toString()))
        return challengeMetadatas
    }
}