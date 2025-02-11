package org.airabinovich.simplebookingsmanager.error

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

sealed class CustomError(val message: String) {
    override fun toString(): String {
        return message
    }
}

class UnexpectedError(
    message: String,
    @JsonIgnoreProperties("stackTrace", "suppressed", "localizedMessage", "cause")
    val cause: Throwable? = null,
) : CustomError(message)

sealed class EntityNotFoundError(entityName: String) : CustomError(
    message = "Entity $entityName not found",
)

class UserNotFoundError : EntityNotFoundError("user")

sealed class EntityAlreadyExistsError(entityName: String) : CustomError(
    message = "Entity $entityName already exists",
)

class UserAlreadyExistsError : EntityAlreadyExistsError("user")
