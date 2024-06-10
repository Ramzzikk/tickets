package com.tickets.users

import com.tickets.db.dao.UserDAO
import com.tickets.db.tables.UsersTable
import com.tickets.models.user.UserCreate
import com.tickets.models.user.validation.accessors.email
import com.tickets.models.user.validation.accessors.nickname
import com.tickets.models.user.validation.accessors.password
import com.tickets.utils.suspendTransaction
import com.tickets.utils.validateModel
import dev.nesk.akkurate.Validator
import dev.nesk.akkurate.constraints.builders.hasLengthGreaterThanOrEqualTo
import dev.nesk.akkurate.constraints.builders.isMatching
import dev.nesk.akkurate.constraints.builders.isNotBlank
import dev.nesk.akkurate.constraints.constrain
import dev.nesk.akkurate.constraints.otherwise
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

val validateUserCreate = Validator.suspendable<UserCreate> {
    nickname.isNotBlank()
    email.isMatching(Regex("""^[\w.-]+@([\w-]+\.)+[\w-]{2,4}$""")).otherwise { "Wrong email format" }
    password {
        hasLengthGreaterThanOrEqualTo(8)
        isMatching(Regex("""^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$"""))
            .otherwise {
                "Password must contain at least one uppercase and lowercase letter, one number and a special character"
            }
    }
    email.constrain {
        suspendTransaction {
            UserDAO.find { UsersTable.email eq email.unwrap() }.empty()
        }
    }.otherwise { "User with email ${email.unwrap()} already exists" }
}

fun Application.usersValidation() {
    install(RequestValidation) {
        validate<UserCreate> {
            validateModel(it, validateUserCreate)
        }
    }
}
