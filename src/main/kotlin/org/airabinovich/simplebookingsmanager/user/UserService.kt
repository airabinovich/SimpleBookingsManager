package org.airabinovich.simplebookingsmanager.user

import arrow.core.Either
import arrow.core.Option
import arrow.core.left
import arrow.core.raise.either
import arrow.core.right
import jakarta.persistence.EntityNotFoundException
import org.airabinovich.simplebookingsmanager.error.CustomError
import org.airabinovich.simplebookingsmanager.error.UnexpectedError
import org.airabinovich.simplebookingsmanager.error.UserNotFoundError
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService @Autowired constructor(
    private val userRepository: UserRepository
) {

    fun upsertUser(userDto: UserDto): Either<CustomError, UserDto> = try {
        val existingUser = userRepository.findByNameAndLastName(userDto.name!!, userDto.lastName!!)
        if (userDto.id == null && existingUser.isNotEmpty()) {
            userRepository.save(User.fromDto(userDto.copy(id = existingUser.first().id))).toDto().right()
        } else {
            userRepository.save(User.fromDto(userDto)).toDto().right()
        }

    } catch (ex: EntityNotFoundException) {
        UserNotFoundError(ex).left()
    } catch (ex: Exception) {
        UnexpectedError("error getting user", cause = ex).left()
    }

    fun getUser(userId: Long): Either<CustomError, Option<UserDto>> = either {
        Option.fromNullable(userRepository.findById(userId).orElse(null))
            .filter { it.active }
            .map { it.toDto() }
    }

    fun deleteUser(userId: Long): Either<CustomError, Unit> = either {
        val user = Option.fromNullable(userRepository.findById(userId).orElse(null))
        user.map { usr -> usr.copy(active = false) }
            .onSome { userRepository.save(it) }
    }

}
