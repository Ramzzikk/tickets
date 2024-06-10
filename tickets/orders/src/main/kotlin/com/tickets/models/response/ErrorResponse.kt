package com.tickets.models.response

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val error: String,
)
