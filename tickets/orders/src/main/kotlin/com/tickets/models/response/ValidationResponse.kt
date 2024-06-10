package com.tickets.models.response

import kotlinx.serialization.Serializable

@Serializable
data class ValidationResponse(
    val validationErrors: List<String>
)
