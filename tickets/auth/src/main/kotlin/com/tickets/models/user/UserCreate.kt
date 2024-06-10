package com.tickets.models.user

import dev.nesk.akkurate.annotations.Validate
import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

@Validate
@Serializable
data class UserCreate(
    @field:Schema(required = true) val nickname: String,
    @field:Schema(required = true) val email: String,
    @field:Schema(required = true) val password: String
)
