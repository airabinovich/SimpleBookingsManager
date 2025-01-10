package org.airabinovich.simplebookingsmanager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class SimpleBookingsManagerApplication

fun main(args: Array<String>) {
    runApplication<SimpleBookingsManagerApplication>(*args)
}
