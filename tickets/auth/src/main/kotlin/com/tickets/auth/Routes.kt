package com.tickets.auth

import com.tickets.db.dao.UserDAO
import com.tickets.db.tables.UsersTable
import com.tickets.jwt.JwtData
import com.tickets.jwt.JwtHelper
import com.tickets.models.response.ErrorResponse
import com.tickets.models.response.ValidationResponse
import com.tickets.models.user.UserLogin
import com.tickets.utils.PasswordsHelper
import com.tickets.utils.getJson
import com.tickets.utils.suspendTransaction
import io.github.smiley4.ktorswaggerui.dsl.post
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.authRoutes() {
    val passwordSecret = environment.config.property("passwords.secret").getString()
    val passwordsHelper = PasswordsHelper(passwordSecret)

    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtIssuer = environment.config.property("jwt.issuer").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()

    val jwtHelper = JwtHelper(jwtSecret, jwtAudience, jwtIssuer)

    routing {
        post("/login", {
            description = "Авторизация и получение JWT-токена"
            request {
                body<UserLogin> {
                    required = true
                }
            }
            response {
                HttpStatusCode.OK to {
                    description = "Успешно"
                    body<JwtData>()
                }
                HttpStatusCode.UnprocessableEntity to {
                    description = "Неверный формат данных"
                    body<ValidationResponse>()
                }
                HttpStatusCode.BadRequest to {
                    description = "Неверные авторизационные данные"
                    body<ErrorResponse>()
                }
            }
        }) {
            val loginData = try {
                call.receive<UserLogin>()
            } catch (e: BadRequestException) {
                getJson().decodeFromString<UserLogin>(call.receiveText())
                return@post
            }

            val user = suspendTransaction {
                UserDAO.find { UsersTable.email eq loginData.email }.firstOrNull()
            }

            if (user == null) {
                return@post call.respond(HttpStatusCode.BadRequest, ErrorResponse("User with email '${loginData.email}' does not exist"))
            }

            if (!passwordsHelper.validatePassword(loginData.password, user.password)) {
                return@post call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid password"))
            }

            val token = jwtHelper.createToken(user.id.value)
            call.respond(token)
        }
    }
}
