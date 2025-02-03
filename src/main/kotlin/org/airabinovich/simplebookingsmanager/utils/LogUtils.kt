package org.airabinovich.simplebookingsmanager.utils

import org.airabinovich.simplebookingsmanager.error.CustomError
import org.airabinovich.simplebookingsmanager.error.UnexpectedError
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.spi.LoggingEventBuilder

object LogUtils {

    inline fun <reified T> T.logger(): Logger {
        return LoggerFactory.getLogger(T::class.java)
    }

    fun LoggingEventBuilder.attach(error: CustomError): LoggingEventBuilder {
        if (error is UnexpectedError) {
            error.cause?.let {
                this.setCause(it)
            }
        }
        return this.addKeyValue("error", error)
    }
}
