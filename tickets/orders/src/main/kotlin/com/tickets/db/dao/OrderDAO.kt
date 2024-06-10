package com.tickets.db.dao

import com.tickets.db.tables.OrdersTable
import com.tickets.models.order.Order
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class OrderDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<OrderDAO>(OrdersTable)

    var userId by OrdersTable.userId
    var fromStationId by OrdersTable.fromStationId
    var toStationId by OrdersTable.toStationId
    var status by OrdersTable.status
    private var created by OrdersTable.created

    fun toOrder() = Order(
        id.value,
        userId,
        fromStationId.value,
        toStationId.value,
        status,
        created.toString()
    )
}