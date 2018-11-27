package main.daos

import framework.models.BaseIntEntity
import framework.models.BaseIntEntityClass
import framework.models.BaseIntIdTable
import org.jetbrains.exposed.dao.EntityID

/**
 * Currently only housing who can validate the completion of a challenge
 * Eventually this will house things like smart contract completers.
 *
 * @property id
 * @property address The address which can trigger validation of completion
 * @property reward the reward amount and it's pool
 */
class CompletionCriteria(id: EntityID<Int>) : BaseIntEntity(id, CompletionCriterias) {
    companion object : BaseIntEntityClass<CompletionCriteria>(CompletionCriterias)

    var address by CompletionCriterias.address
    var reward by CompletionCriterias.reward
}

object CompletionCriterias : BaseIntIdTable("completion_criterias") {
    val address = varchar("address", 256).uniqueIndex()
    val reward = reference("reward", Rewards)
}