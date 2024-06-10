package com.tickets.plugins

import com.tickets.db.dao.UserDAO
import com.tickets.jwt.JwtHelper
import com.tickets.utils.suspendTransaction
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity() {
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtIssuer = environment.config.property("jwt.issuer").getString()
    val jwtRealm = environment.config.property("jwt.realm").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()

    val jwtHelper = JwtHelper(jwtSecret, jwtAudience, jwtIssuer)

    authentication {
        jwt("JWT") {
            realm = jwtRealm
            verifier(jwtHelper.verifier)
            validate { credential ->
                if (credential.payload.subject.toIntOrNull() != null) {
                    val user = suspendTransaction {
                        UserDAO.findById(credential.payload.subject.toInt())
                    }
                    if (user != null) {
                        JWTPrincipal(credential.payload)
                    } else {
                        null
                    }
                } else {
                    null
                }
            }
        }
    }
}
