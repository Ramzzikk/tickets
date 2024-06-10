package com.tickets.models.order

import com.tickets.db.enums.OrderStatus
import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val id: Int,
    val userId: Int,
    val fromStation: Int,
    val toStation: Int,
    val status: OrderStatus = OrderStatus.CHECK,
    val created: String
)
