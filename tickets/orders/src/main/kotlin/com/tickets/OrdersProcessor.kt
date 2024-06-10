package com.tickets

import com.tickets.db.dao.OrderDAO
import com.tickets.db.enums.OrderStatus
import com.tickets.db.tables.OrdersTable
import com.tickets.plugins.configureDatabases
import com.tickets.utils.suspendTransaction
import kotlinx.coroutines.delay
import kotlin.random.Random

suspend fun main() {
    configureDatabases()

    while (true) {
        suspendTransaction {
            OrderDAO.find { OrdersTable.status eq OrderStatus.CHECK }
        }.forEach { order ->
            delay(Random.nextLong(200, 1000))
            val nextStatus = listOf(OrderStatus.SUCCESS, OrderStatus.REJECTION).random()

            suspendTransaction {
                order.status = nextStatus
            }
        }

        delay(10000)
    }
}