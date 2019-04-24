package main.daos

import framework.models.BaseEntityNamespace

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
class Challenge(
    val cryptoKeyPair: CryptoKeyPairNamespace,
    val challengeSettings: ChallengeSettingNamespace,
    val challengeType: ChallengeType,
    val distributionFeeReward: RewardNamespace? = null,
    val parentChallenge: String? = null
): BaseEntityNamespace() {

    override fun toMap(): MutableMap<String, Any?> {
        val map = super.toMap()
        map.putAll(challengeSettings.toMap())
        map.putAll(cryptoKeyPair.toMap())
        map.put("challengeType", challengeType.type)
        if(parentChallenge != null)
            map["parentChallenge"] = parentChallenge
        if(distributionFeeReward != null)
            map.putAll(distributionFeeReward.toMap())
        return map
    }
}

data class ChallengeToUnsharedTransactionNamespace(val challenge: ChallengeNamespace, val shareTransactionList: ShareTransactionListNamespace)

data class UserAccountChallengerNamespace(val challenger: UserAccountNamespace, val receivers: List<UserAccountChallengerNamespace>)

data class ChallengeToUnsharedTransactionsNamespaceList(val challengeToUnsharedTransactions: List<ChallengeToUnsharedTransactionNamespace>)

data class SubChallengeNamespace(val subChallengeId: Int?, val type: String?)

data class ChallengeNamespace(
    val challengeSettings: ChallengeSettingNamespace,
    val completionCriteria: CompletionCriteriaNamespace,
    val distributionFeeReward: RewardNamespace,
    val subChallenges: List<SubChallengeNamespace>? = null,
    val parentChallenge: String? = null
    )

enum class ChallengeType(val type: String) {
    SYNC("sync"), ASYNC("async")
}