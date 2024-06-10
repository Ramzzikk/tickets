package com.tickets.plugins

import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.github.smiley4.ktorswaggerui.data.AuthScheme
import io.github.smiley4.ktorswaggerui.data.AuthType
import io.ktor.server.application.*

fun Application.configureSwagger() {
    install(SwaggerUI) {
        swagger {
            swaggerUrl = "docs"
        }
        info {
            title = "Auth API"
            version = "1.0.0"
            description = "API авторизации в сервисе продажи билетов"
        }
        server {
            description = "Default Server"
        }

        securityScheme("JWT") {
            type = AuthType.HTTP
            scheme = AuthScheme.BEARER
            bearerFormat = "JWT"
        }

        defaultUnauthorizedResponse {
            description = "Unauthorized user"
        }

        defaultSecuritySchemeName = "JWT"
    }
}
