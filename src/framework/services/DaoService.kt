package framework.services

import kotlinserverless.framework.models.*

object DaoService {
    fun throwOrReturn(message: String?) {
        when {
            message?.equals("Invalid api credentials") == true -> {
                throw UnauthorizedError(message!!)
            }
            message?.contains("Cannot transition from") == true ->  {
                throw ForbiddenException(message)
            }
            message?.contains("User not permitted") == true ->  {
                throw ForbiddenException(message)
            }
            message?.contains("You do not have enough") == true ->  {
                throw ForbiddenException(message)
            }
            message?.contains("has not been") == true ->  {
                throw ForbiddenException(message)
            }
            message?.contains("user cannot") == true -> {
                throw ForbiddenException(message)
            }
            else -> null
        }
    }
}