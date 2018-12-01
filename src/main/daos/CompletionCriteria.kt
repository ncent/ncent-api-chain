package main.daos

import framework.models.BaseIntEntity
import framework.models.BaseIntEntityClass
import framework.models.BaseIntIdTable
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.Table
import org.joda.time.DateTime

/**
 * Currently only housing who can validate the completion of a challenge
 * Eventually this will house things like smart contract completers.
 *
 * @property id
 * @property address The address which can trigger validation of completion
 * @property reward the reward amount and it's pool
 * @property expiration
 * @property prereq If this completion criteria can only be completed if another is completed
 */
class CompletionCriteria(id: EntityID<Int>) : BaseIntEntity(id, CompletionCriterias) {
    companion object : BaseIntEntityClass<CompletionCriteria>(CompletionCriterias)

    var address by CompletionCriterias.address
    var reward by Reward referencedOn CompletionCriterias.reward
    var expiration by CompletionCriterias.expiration
    var prereq by CompletionCriteria via PrerequisiteCompletionCriterias
}

object PrerequisiteCompletionCriterias : Table("prerequisite_completion_criterias") {
    var completionCriteria = reference("completion_criteria_to_prereq", CompletionCriterias).primaryKey()
    var prereqCompletionCriteria = reference("prereq_to_completion_criteria", CompletionCriterias).primaryKey()
}

object CompletionCriterias : BaseIntIdTable("completion_criterias") {
    val address = varchar("address", 256)
    val reward = reference("reward", Rewards)
    val expiration = datetime("expiration")
}

data class CompletionCriteriaNamespace(val address: String?, val rewardNamespace: RewardNamespace, val expiration: DateTime, val preReqCompletionCriteriaIds: List<Int>)