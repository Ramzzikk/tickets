package com.tickets.models

import kotlinx.serialization.Serializable

@Serializable
data class Station(
    val id: Int,
    val station: String
)
