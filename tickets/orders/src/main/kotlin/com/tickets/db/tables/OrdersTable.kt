package com.tickets.db.tables

import com.tickets.db.enums.OrderStatus
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.IntegerColumnType
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object OrdersTable : IntIdTable(name = "orders") {
    val userId = integer("user_id")
    val fromStationId = reference("from_station_id", StationsTable)
    val toStationId = reference("to_station_id", StationsTable)
    val status = customEnumeration("status", IntegerColumnType().sqlType(),
        fromDb = { status -> OrderStatus.entries.first { it.id == status.toString().toInt() } },
        toDb = { it.id }
    ).default(OrderStatus.CHECK)
    val created = datetime("created").defaultExpression(CurrentDateTime)
}