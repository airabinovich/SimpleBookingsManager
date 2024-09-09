package org.airabinovich.simplebookingsmanager.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

    fun findByNameAndLastName(name: String, lastName: String): List<User>
}
