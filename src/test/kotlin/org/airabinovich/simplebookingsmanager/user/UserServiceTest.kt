package org.airabinovich.simplebookingsmanager.user

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional

@Transactional
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceTest @Autowired constructor(
    private val userService: UserService,
) {

    @Test
    fun createUser() {
        val user = UserDto(
            name = "testUser",
            lastName = "testUserLastName",
            email = "tes@email.com",
        )

        val retrievedUser = userService.upsertUser(user)

        assertTrue(retrievedUser.isRight())
        val savedUser = retrievedUser.getOrNull()!!

        assertEquals(user.name, savedUser.name)
        assertEquals(user.lastName, savedUser.lastName)
    }
}
