package org.airabinovich.simplebookingsmanager.user

import arrow.core.*
import arrow.core.raise.either
import arrow.core.raise.ensure
import org.airabinovich.simplebookingsmanager.error.CustomError
import org.airabinovich.simplebookingsmanager.error.UnexpectedError
import org.airabinovich.simplebookingsmanager.error.UserNotFoundError
import org.airabinovich.simplebookingsmanager.utils.LogUtils
import org.airabinovich.simplebookingsmanager.utils.LogUtils.attach
import org.airabinovich.simplebookingsmanager.utils.LogUtils.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService @Autowired constructor(
    private val userRepository: UserRepository
) {

    private val logger = LogUtils.logger()

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
                logger.atError().attach(err).log("error saving user. ${err.message}")
                UnexpectedError("error saving user")
            }
    }

    fun upsertUserWithBind(userDto: UserDto): Either<CustomError, UserDto> = either {
        val userToSave = if (userDto.id == null) {
            User.fromDto(userDto)
        } else {
            userRepository.findById(userDto.id)
                .toEither { UserNotFoundError() }
                .bind()
        }

        val savedUser = userRepository.save(userToSave).bind()

        return savedUser.toDto().right()
    }

    fun getOptionUser(userId: Long): Either<CustomError, Option<UserDto>> = either {
        ensure(userId != 100L) { // Error case added to show the handling
            val unexpectedError = UnexpectedError("error getting user", RuntimeException("DB crashed"))
            logger.atError().attach(unexpectedError).log(unexpectedError.message)
            unexpectedError
        }
        userRepository.findById(userId)
            .filter { it.active }
            .map { it.toDto() }
    }

    fun getUser(userId: Long): Either<CustomError, UserDto> {
        return if (userId == 100L) { // Error case added to show the handling
            val errorMessage = "error getting user"
            logger.atError().attach(UnexpectedError(errorMessage)).log(errorMessage)
            UnexpectedError(errorMessage).left()
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
