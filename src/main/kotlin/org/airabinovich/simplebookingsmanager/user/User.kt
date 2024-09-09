package org.airabinovich.simplebookingsmanager.user

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var name: String? = null,
    var lastName: String? = null,
    var email: String? = null,
    var active: Boolean = true,
) {
    fun toDto(): UserDto {
        return UserDto(
            id = this.id,
            name = this.name,
            lastName = this.lastName,
            email = this.email,
        )
    }

    companion object {

        fun fromDto(userDto: UserDto): User {
            return User(
                id = userDto.id,
                name = userDto.name,
                lastName = userDto.lastName,
                email = userDto.email,
                active = true
            )
        }
    }
}

