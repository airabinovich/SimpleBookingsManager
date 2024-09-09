package org.airabinovich.simplebookingsmanager.error

abstract class CustomError(protected val message: String, protected val cause: Throwable? = null) {
    override fun toString(): String {
        return message
    }
}

class UnexpectedError(message: String, cause: Throwable? = null) : CustomError(message, cause)

abstract class EntityNotFoundError(entityName : String, cause: Throwable? = null) : CustomError(
    message = "Entity $entityName not found",
    cause = cause
)
class UserNotFoundError(cause: Throwable? = null) : EntityNotFoundError("user", cause)
