package com.tickets.models.user

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val nickname: String,
    val email: String,
    val created: String
)
