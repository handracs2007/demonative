package com.handra.demo.demonative

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.dao.DataAccessException
import org.springframework.data.repository.CrudRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@SpringBootApplication
class DemonativeApplication

fun main(args: Array<String>) {
    runApplication<DemonativeApplication>(*args)
}

@Entity
@Table(name = "user")
data class User(
    @Id
    @Column(name = "username", length = 255, nullable = false)
    var username: String,

    @Column(name = "fullname", length = 255, nullable = false)
    var fullname: String,
)

@Repository
interface UserRepository : CrudRepository<User, String>

data class AddUserRequest(
    val username: String,
    val fullname: String,
)

@RestController
class UserController(
    private val userRepository: UserRepository,
) {

    @GetMapping("/users")
    fun users(): ResponseEntity<List<User>> = ResponseEntity.ok(this.userRepository.findAll().toList())

    @PostMapping("/user")
    fun addUser(@RequestBody addUserRequest: AddUserRequest): ResponseEntity<Void> {
        return try {
            val newUser = User(username = addUserRequest.username, fullname = addUserRequest.fullname)
            this.userRepository.save(newUser)
            ResponseEntity.ok().build()
        } catch (ex: DataAccessException) {
            ResponseEntity.internalServerError().build()
        }
    }
}