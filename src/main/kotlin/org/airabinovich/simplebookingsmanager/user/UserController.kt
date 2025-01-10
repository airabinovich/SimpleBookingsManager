package org.airabinovich.simplebookingsmanager.user

import arrow.core.Either
import arrow.core.Option
import arrow.core.getOrElse
import org.airabinovich.simplebookingsmanager.error.CustomError
import org.airabinovich.simplebookingsmanager.error.UserNotFoundError
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user")
class UserController @Autowired constructor(
    private val userService: UserService,
) {

    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: Long): Either<CustomError, UserDto> = userService.getUser(userId)

    @GetMapping("/option/{userId}")
    fun getOptionUser(@PathVariable userId: Long): Either<CustomError, Option<UserDto>> = userService.getOptionUser(userId)

    @PostMapping
    fun upsertUser(@RequestBody user: UserDto): ResponseEntity<UserDto> = userService.upsertUser(user)
        .map { userResponse -> ResponseEntity.ok(userResponse) }
        .getOrElse { ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR) }


    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable("userId") userId: Long): ResponseEntity<UserDto> = userService.deleteUser(userId)
        .map { ResponseEntity<UserDto>(HttpStatus.NO_CONTENT) }
        .getOrElse { err ->
            if (err is UserNotFoundError) {
                ResponseEntity(HttpStatus.NOT_FOUND)
            } else {
                ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
            }
        }


}
