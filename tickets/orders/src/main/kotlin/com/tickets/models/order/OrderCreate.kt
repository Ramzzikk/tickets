package com.tickets.models.order

import dev.nesk.akkurate.annotations.Validate
import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

@Validate
@Serializable
data class OrderCreate(
    @field:Schema(required = true) val fromStation: Int,
    @field:Schema(required = true) val toStation: Int,
)
