package org.airabinovich.simplebookingsmanager.error

abstract class CustomError(message: String, cause: Throwable? = null) : Throwable(message, cause) {
    override fun toString(): String {
        return message!!
    }
}

class UnexpectedError(message: String, cause: Throwable? = null) : CustomError(message, cause)

open class EntityNotFoundError(entityName : String, cause: Throwable? = null) : CustomError(
    message = "Entity $entityName not found",
    cause = cause
)
class UserNotFoundError(cause: Throwable? = null) : EntityNotFoundError("user", cause)

abstract class EntityAlreadyExistsError(entityName : String, cause: Throwable? = null) : CustomError(
    message = "Entity $entityName already exists",
    cause = cause
)
class UserAlreadyExistsError(cause: Throwable? = null) : EntityAlreadyExistsError("user", cause)
