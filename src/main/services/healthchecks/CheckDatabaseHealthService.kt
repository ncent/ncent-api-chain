package main.services.healthchecks

import kotlinserverless.framework.models.Handler

object CheckDatabaseHealthService {
    fun execute() {
        Handler.connectToLedger()
    }
}