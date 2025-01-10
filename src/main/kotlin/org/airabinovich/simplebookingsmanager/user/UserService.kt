package org.airabinovich.simplebookingsmanager.user

import arrow.core.*
import arrow.core.raise.either
import arrow.core.raise.ensure
import org.airabinovich.simplebookingsmanager.error.CustomError
import org.airabinovich.simplebookingsmanager.error.UnexpectedError
import org.airabinovich.simplebookingsmanager.error.UserNotFoundError
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService @Autowired constructor(
    private val userRepository: UserRepository
) {

    fun upsertUser(userDto: UserDto): Either<CustomError, UserDto> {
        val userToSave: Either<CustomError, User> = if (userDto.id != null) {
            val existingUser = userRepository.findById(userDto.id)
            existingUser.toEither { UserNotFoundError() }
        } else {
            User.fromDto(userDto).right()
        }

        return userToSave
            .flatMap { userRepository.save(it) }
            .map { it.toDto() }
            .mapLeft { err ->
                UnexpectedError("error getting user", cause = err)
            }
    }

    fun getOptionUser(userId: Long): Either<CustomError, Option<UserDto>> = either {
        ensure(userId != 100L) {
            UnexpectedError("error getting user")
        }
        userRepository.findById(userId)
            .filter { it.active }
            .map { it.toDto() }
    }

    fun getUser(userId: Long): Either<CustomError, UserDto> {
        return if (userId == 100L) {
            UnexpectedError("error getting user").left()
        } else {
            userRepository
                .findById(userId)
                .filter { it.active }
                .map { it.toDto() }
                .toEither { UserNotFoundError() }
        }
    }

    fun deleteUser(userId: Long): Either<CustomError, Unit> = either {
        userRepository.findById(userId).map { usr -> usr.copy(active = false) }
            .onSome { userRepository.save(it) }
    }

}
