package org.airabinovich.simplebookingsmanager.user

import arrow.core.Either
import arrow.core.Option
import arrow.core.left
import arrow.core.right
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceException
import org.airabinovich.simplebookingsmanager.error.CustomError
import org.airabinovich.simplebookingsmanager.error.UnexpectedError
import org.airabinovich.simplebookingsmanager.error.UserAlreadyExistsError
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
open class PostgresDatabaseUserRepository(
    private val entityManager: EntityManager,
) : UserRepository {

    @Transactional(readOnly = true)
    override fun findById(id: Long): Option<User> {
        return Option.fromNullable(entityManager.find(User::class.java, id))
    }

    @Transactional(readOnly = true)
    override fun findByNameAndLastName(name: String, lastName: String): List<User> {
        return entityManager.createQuery(
            "SELECT User from User u where u.name = :name and u.lastName = :lastName",
            User::class.java
        )
            .setParameter("name", name)
            .setParameter("lastName", lastName)
            .resultList
    }

    @Transactional
    override fun save(user: User): Either<CustomError, User> {
        try {
            val persistedUser = if (user.id == null) {
                entityManager.persist(user)
                user
            } else {
                entityManager.merge(user)
            }
            return persistedUser.right()
        } catch (ex: PersistenceException) {
            return UserAlreadyExistsError(ex).left()
        } catch (ex: Exception) {
            return UnexpectedError("Error saving user", ex).left()
        }

    }
}
