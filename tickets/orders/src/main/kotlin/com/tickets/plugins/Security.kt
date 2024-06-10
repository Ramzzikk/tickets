package com.tickets.plugins

import com.tickets.models.User
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureSecurity() {
    authentication {
        bearer("JWT") {
            realm = "orders"
            authenticate {
                val client = HttpClient(CIO) {
                    install(ContentNegotiation) {
                        json()
                    }
                    install(Auth) {
                        bearer {
                            loadTokens {
                                BearerTokens(it.token, "")
                            }
                        }
                    }
                }
                val response = client.get("http://auth-api:8080/users")
                if (response.status == HttpStatusCode.OK) {
                    response.body<User>()
                } else {
                    null
                }
            }
        }
    }
}
