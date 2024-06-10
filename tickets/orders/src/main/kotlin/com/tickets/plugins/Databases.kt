package com.tickets.plugins

import com.tickets.db.dao.StationDAO
import com.tickets.db.tables.OrdersTable
import com.tickets.db.tables.StationsTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun configureDatabases() {
    Database.connect(
        url = "jdbc:postgresql://orders-db:5432/orders",
        user = "postgres",
    )

    transaction {
        SchemaUtils.create(StationsTable, OrdersTable)
        if (StationDAO.count() == 0L) {
            val stations = listOf("Москва", "Санкт-Петербург", "Нижний Новгород", "Казань", "Екатеринбург")
            stations.forEach { stationName ->
                StationDAO.new {
                    station = stationName
                }
            }
        }
    }
}
