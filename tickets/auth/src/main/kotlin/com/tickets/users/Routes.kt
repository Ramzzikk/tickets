package com.tickets.users

import com.tickets.db.dao.UserDAO
import com.tickets.models.response.ValidationResponse
import com.tickets.models.user.User
import com.tickets.models.user.UserCreate
import com.tickets.utils.PasswordsHelper
import com.tickets.utils.getJson
import com.tickets.utils.suspendTransaction
import io.github.smiley4.ktorswaggerui.dsl.get
import io.github.smiley4.ktorswaggerui.dsl.post
import io.github.smiley4.ktorswaggerui.dsl.route
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.usersRoutes() {
    val passwordSecret = environment.config.property("passwords.secret").getString()
    val passwordsHelper = PasswordsHelper(passwordSecret)

    routing {
        route("users") {
            authenticate("JWT") {
                get({
                    description = "Получение информации о пользователе"
                    response {
                        HttpStatusCode.OK to {
                            description = "Успешно"
                            body<User>()
                        }
                    }
                }) {
                    val principal = call.principal<JWTPrincipal>()!!

                    val user = suspendTransaction {
                        UserDAO[principal.payload.subject.toInt()]
                    }.toUser()

                    call.respond(user)
                }
            }

            post({
                description = "Регистрация"
                request {
                    body<UserCreate> {
                        required = true
                    }
                }
                response {
                    HttpStatusCode.Created to {
                        description = "Успешно"
                        body<User>()
                    }
                    HttpStatusCode.UnprocessableEntity to {
                        description = "Неверный формат данных"
                        body<ValidationResponse>()
                    }
                }
            }) {
                val user = try {
                    call.receive<UserCreate>()
                } catch (e: BadRequestException) {
                    getJson().decodeFromString<UserCreate>(call.receiveText())
                    return@post
                }

                val newUser = suspendTransaction {
                     UserDAO.new {
                        nickname = user.nickname
                        email = user.email
                        password = passwordsHelper.hashPassword(user.password)
                    }
                }

                call.respond(HttpStatusCode.Created, newUser.toUser())
            }
        }
    }
}
