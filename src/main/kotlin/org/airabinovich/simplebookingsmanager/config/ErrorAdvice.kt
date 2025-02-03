package org.airabinovich.simplebookingsmanager.config

import arrow.core.Either
import arrow.core.None
import arrow.core.Some
import org.airabinovich.simplebookingsmanager.error.ErrorCodeMapper.determineHttpErrorCode
import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@RestControllerAdvice
class ErrorAdvice : ResponseBodyAdvice<Any> {

    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return true
    }

    override tailrec fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        return when (body) {
            // Either handling
            is Either.Right<*> -> beforeBodyWrite(
                body.value,
                returnType,
                selectedContentType,
                selectedConverterType,
                request,
                response
            )

            is Either.Left<*> -> {
                response.setStatusCode(determineHttpErrorCode(body.value))
                beforeBodyWrite(
                    body.value,
                    returnType,
                    selectedContentType,
                    selectedConverterType,
                    request,
                    response
                )
            }

            // Option handling
            is Some<*> -> beforeBodyWrite(
                body.value,
                returnType,
                selectedContentType,
                selectedConverterType,
                request,
                response
            )

            is None -> {
                response.setStatusCode(HttpStatus.NO_CONTENT)
            }

            else -> body
        }
    }

}
