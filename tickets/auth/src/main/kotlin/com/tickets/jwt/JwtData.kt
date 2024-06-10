package com.tickets.jwt

import kotlinx.serialization.Serializable

@Serializable
data class JwtData(
    val accessToken: String,
    val expires: Long
)
