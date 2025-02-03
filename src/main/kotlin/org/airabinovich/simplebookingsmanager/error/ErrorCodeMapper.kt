package org.airabinovich.simplebookingsmanager.error

import org.springframework.http.HttpStatus

object ErrorCodeMapper {

    fun determineHttpErrorCode(error: Any?): HttpStatus = when(error) {
        is EntityNotFoundError -> HttpStatus.NOT_FOUND
        is EntityAlreadyExistsError -> HttpStatus.BAD_REQUEST
        is UnexpectedError, is CustomError -> HttpStatus.INTERNAL_SERVER_ERROR
        else -> HttpStatus.INTERNAL_SERVER_ERROR
    }
}
