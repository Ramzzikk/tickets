package com.tickets.models

import io.ktor.server.auth.*
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val nickname: String,
    val email: String,
    val created: String
) : Principal
