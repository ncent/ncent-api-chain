package main.daos

import com.amazonaws.services.simpledb.model.ReplaceableAttribute
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
data class Challenge(
    val cryptoKeyPair: CryptoKeyPair,
    val challengeSettings: ChallengeSettingNamespace,
    val challengeType: ChallengeType,
    val distributionFeeReward: RewardNamespace? = null,
    val parentChallenge: String? = null
): BaseEntityNamespace() {
    override val className: String
        get() = "challenge"

    override fun getAttributes(): MutableList<ReplaceableAttribute> {
        val list = super.getAttributes()
        list.addAll(challengeSettings.getAttributes())
        list.addAll(cryptoKeyPair.getAttributes())
        list.add(ReplaceableAttribute("challengeType", challengeType.type, true))
        if(parentChallenge != null)
            list.add(ReplaceableAttribute("parentChallenge", parentChallenge, true))
        if(distributionFeeReward != null)
            list.addAll(distributionFeeReward.getAttributes())
        return list
    }

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

data class ChallengeToUnsharedTransactionNamespace(val challenge: Challenge, val shareTransactionList: ShareTransactionListNamespace)

data class UserAccountChallengerNamespace(val challenger: UserAccountNamespace, val receivers: List<UserAccountChallengerNamespace>)

data class ChallengeToUnsharedTransactionsNamespaceList(val challengeToUnsharedTransactions: List<ChallengeToUnsharedTransactionNamespace>)

data class SubChallengeNamespace(val subChallengeId: Int?, val type: String?)

enum class ChallengeType(val type: String) {
    SYNC("sync"), ASYNC("async")
}