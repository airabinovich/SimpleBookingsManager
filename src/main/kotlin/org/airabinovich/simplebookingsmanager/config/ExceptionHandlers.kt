package org.airabinovich.simplebookingsmanager.config

import org.airabinovich.simplebookingsmanager.error.CustomError
import org.airabinovich.simplebookingsmanager.error.EntityNotFoundError
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class ExceptionHandlers: ResponseEntityExceptionHandler() {

    @ExceptionHandler(EntityNotFoundError::class)
    fun handleNotFoundException(e: EntityNotFoundError): ProblemDetail {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message)
        problemDetail.title = "Entity Not Found"
        return problemDetail
    }

    @ExceptionHandler(CustomError::class)
    fun handleCustomError(e: CustomError): ProblemDetail {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        problemDetail.title = "Unexpected Error"
        return problemDetail
    }
}
