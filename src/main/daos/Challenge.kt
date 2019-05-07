package main.daos

import com.amazonaws.services.simpledb.model.ReplaceableAttribute
import framework.models.BaseEntityNamespace
import org.joda.time.DateTime

/**
 * Used to represent challenges and store pointers to models that
 * store the state for a challenge over the lifecycle of that challenge
 *
 * @property id
 * @property parentChallenge the parent of this challenge, typically this challenge must be completed before the parent
 * @property challengeSettings ChallengeSettings
 * @property completionCriteria CompletionCriteria
 * @property distributionFeeReward the distribution fees and the pool. this is the
 * pool that will be drawn on if anybody 'opts-out' of attempting to help.
 */
data class Challenge(
    val cryptoKeyPair: CryptoKeyPair,
    val challengeSettings: ChallengeSetting,
    val challengeType: ChallengeType,
    val completionCriteria: CryptoKeyPair,
    val reward: Reward,
    val distributionFeeReward: Reward? = null,
    val parentChallenge: String? = null,
    val preReqs: List<String>? = null
): BaseEntityNamespace() {
    override fun getAttributes(): MutableList<ReplaceableAttribute> {
        val list = super.getAttributes()
        list.addAll(challengeSettings.getAttributes())
        list.addAll(cryptoKeyPair.getAttributes())
        list.addAll(reward.getAttributes())
        list.addAll(completionCriteria.getAttributes())
        list.add(ReplaceableAttribute("challengeType", challengeType.type, true))
        if(parentChallenge != null)
            list.add(ReplaceableAttribute("parentChallenge", parentChallenge, true))
        if(distributionFeeReward != null)
            list.addAll(distributionFeeReward.getAttributes())
        if(preReqs != null)
            list.add(ReplaceableAttribute("preReqs", preReqs.toString(), true))
        return list
    }

    override fun toMap(): MutableMap<String, Any?> {
        val map = super.toMap()
        map.putAll(challengeSettings.toMap())
        map.putAll(cryptoKeyPair.toMap())
        map.put("challengeType", challengeType.type)
        map.putAll(reward.toMap())
        map.putAll(completionCriteria.toMap())
        if(parentChallenge != null)
            map["parentChallenge"] = parentChallenge
        if(distributionFeeReward != null)
            map.putAll(distributionFeeReward.toMap())
        if(preReqs != null)
            map.put("preReqs", preReqs.toString())
        return map
    }
}

data class ChallengeToUnsharedTransactionNamespace(val challenge: Challenge, val shareTransactionList: ShareTransactionListNamespace)

data class UserAccountChallengerNamespace(val challenger: UserAccount, val receivers: List<UserAccountChallengerNamespace>)

data class ChallengeToUnsharedTransactionsNamespaceList(val challengeToUnsharedTransactions: List<ChallengeToUnsharedTransactionNamespace>)

data class SubChallengeNamespace(val subChallengeId: Int?, val type: String?)

enum class ChallengeType(val type: String) {
    SYNC("sync"), ASYNC("async")
}

/**
 * Used to represent challenges settings that are initially setup for every challenge
 *
 * @property name
 * @property expiration
 * @property shareExpiration
 * @property description
 * @property imageUrl
 * @property sponsorName
 * @property admin address of owner of this challenge
 * @property offChain optionally set this challenge to allow off-chain sharing
 * @property maxRewards the number of times the reward can be claimed
 * @property maxDistributionFeeReward the number of times the distribution fee
 * can be rewarded
 * @property maxSharesPerReceivedShare maximum times someone can share a challenge
 * per unique share they recieve
 * @property maxDepth the maximum depth of the providence chain
 * @property maxNodes the maximum number of nodes in the entire share graph
 */
class ChallengeSetting(
    val name: String,
    val description: String,
    val imageUrl: String,
    val sponsorName: String,
    val expiration: DateTime,
    val shareExpiration: DateTime,
    val admin: String,
    val offChain: Boolean,
    val maxShares: Integer?,
    val maxRewards: Integer?,
    val maxDistributionFeeReward: Double?,
    val maxSharesPerReceivedShare: Integer?,
    val maxDepth: Integer?,
    val maxNodes: Integer?,
    val metadatas: Metadatas?
): BaseEntityNamespace() {

    override fun toMap(): MutableMap<String, Any?> {
        var map = super.toMap()
        map.put("name", name)
        map.put("description", description)
        map.put("imageUrl", imageUrl)
        map.put("sponsorName", sponsorName)
        map.put("expiration", expiration.toString())
        map.put("shareExpiration", shareExpiration.toString())
        map.put("admin", admin)
        map.put("offChain", offChain)
        if(maxShares != null)
            map.put("maxShares", maxShares.toString())
        if(maxRewards != null)
            map.put("maxRewards", maxRewards.toString())
        if(maxDistributionFeeReward != null)
            map.put("maxDistributionFeeReward", maxDistributionFeeReward.toString())
        if(maxSharesPerReceivedShare != null)
            map.put("maxSharesPerReceivedShare", maxSharesPerReceivedShare.toString())
        if(maxDepth != null)
            map.put("maxDepth", maxDepth.toString())
        if(maxNodes != null)
            map.put("maxNodes", maxNodes.toString())
        if(metadatas != null)
            map.put("metadatas", metadatas.toMap())
        return map
    }

    override fun getAttributes(): MutableList<ReplaceableAttribute> {
        val list = super.getAttributes()
        list.add(ReplaceableAttribute("name", name, true))
        list.add(ReplaceableAttribute("description", description, true))
        list.add(ReplaceableAttribute("imageUrl", imageUrl, true))
        list.add(ReplaceableAttribute("sponsorName", sponsorName, true))
        list.add(ReplaceableAttribute("expiration", expiration.toString(), true))
        list.add(ReplaceableAttribute("shareExpiration", shareExpiration.toString(), true))
        list.add(ReplaceableAttribute("admin", admin, true))
        list.add(ReplaceableAttribute("offChain", offChain.toString(), true))
        if(maxShares != null)
            list.add(ReplaceableAttribute("maxShares", maxShares.toString(), true))
        if(maxRewards != null)
            list.add(ReplaceableAttribute("maxRewards", maxRewards.toString(), true))
        if(maxDistributionFeeReward != null)
            list.add(ReplaceableAttribute("maxDistributionFeeReward", maxDistributionFeeReward.toString(), true))
        if(maxSharesPerReceivedShare != null)
            list.add(ReplaceableAttribute("maxSharesPerReceivedShare", maxSharesPerReceivedShare.toString(), true))
        if(maxDepth != null)
            list.add(ReplaceableAttribute("maxDepth", maxDepth.toString(), true))
        if(maxNodes != null)
            list.add(ReplaceableAttribute("maxNodes", maxNodes.toString(), true))
        if(metadatas != null)
            list.addAll(metadatas.getAttributes())
        return list
    }
}

/**
 * Reward
 *
 * @property id
 * @property type RewardType
 * @property pool List<RewardPool>
 */
class Reward(
    val type: RewardType,
    val audience: RewardAudience,
    val pool: List<String>,
    val metadatas: Metadatas?
) : BaseEntityNamespace() {
    override fun toMap(): MutableMap<String, Any?> {
        var map = super.toMap()
        map.put("type", type.str)
        map.put("audience", audience.str)
        map.put("pool", pool.toString())
        if(metadatas != null)
            map.putAll(metadatas.toMap())
        return map
    }

    override fun getAttributes(): MutableList<ReplaceableAttribute> {
        var list = super.getAttributes()
        list.add(ReplaceableAttribute("type", type.str, true))
        list.add(ReplaceableAttribute("audience", audience.str, true))
        list.add(ReplaceableAttribute("pool", pool.toString(), true))
        if(metadatas != null)
            list.addAll(metadatas.getAttributes())
        return list
    }
}

/**
 * RewardType signifies the way the rewards will be distributed amongst the entire chain or the providence chain
 *
 * @property id
 * @property audience example: [PROVIDENCE, FULL]
 * @property type example: [SINGLE, EVEN, LOGARITHMIC, EXPONENTIAL]
 */
enum class RewardAudience(val str: String) {
    PROVIDENCE("providence"), FULL("full")
}

enum class RewardType(val str: String) {
    SINGLE("single"), EVEN("even"), LOGARITHMIC("log"), EXPONENTIAL("exp"), N_OVER_2("nover2")
}
