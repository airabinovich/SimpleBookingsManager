package org.airabinovich.simplebookingsmanager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SimpleBookingsManagerApplication

fun main(args: Array<String>) {
    runApplication<SimpleBookingsManagerApplication>(*args)
}
