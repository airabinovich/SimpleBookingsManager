package org.airabinovich.simplebookingsmanager.user

import arrow.core.Either
import arrow.core.Option
import org.airabinovich.simplebookingsmanager.error.CustomError


interface UserRepository {

    fun findById(id: Long): Option<User>

    fun findByNameAndLastName(name: String, lastName: String): List<User>

    fun save(user: User): Either<CustomError, User>
}
