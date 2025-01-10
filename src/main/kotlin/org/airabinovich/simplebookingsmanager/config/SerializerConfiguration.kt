package org.airabinovich.simplebookingsmanager.config

import arrow.core.Either
import arrow.core.Option
import arrow.core.getOrElse
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import org.airabinovich.simplebookingsmanager.error.EntityNotFoundError
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ArrowSerializerConfiguration {

    @Bean
    open fun eitherSerializerMapper(): ObjectMapper {
        val mapper = ObjectMapper()
        val module = SimpleModule()
        module.addSerializer(Either::class.java, EitherSerializer())
        module.addSerializer(Option::class.java, OptionSerializer())
        mapper.registerModule(module)
        return mapper
    }
}


class OptionSerializer : JsonSerializer<Option<*>>() {
    override fun serialize(value: Option<*>, gen: JsonGenerator, serializers: SerializerProvider) {
        value.getOrElse { throw EntityNotFoundError("I don't know who's missing, I lost all context here") }
            ?.let { gen.writeObject(it) }
    }
}


class EitherSerializer : JsonSerializer<Either<*, *>>() {
    override fun serialize(value: Either<*, *>, gen: JsonGenerator, provider: SerializerProvider) {
        when (value) {
            is Either.Left -> when (value.value) {
                is Throwable -> throw value.value as Throwable
                else -> gen.writeObject(value.value)
            }

            is Either.Right -> gen.writeObject(value.value)
        }
    }
}

