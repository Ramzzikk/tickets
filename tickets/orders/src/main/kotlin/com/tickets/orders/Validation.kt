package com.tickets.orders

import com.tickets.db.dao.StationDAO
import com.tickets.models.order.OrderCreate
import com.tickets.models.order.validation.accessors.fromStation
import com.tickets.models.order.validation.accessors.toStation
import com.tickets.utils.suspendTransaction
import com.tickets.utils.validateModel
import dev.nesk.akkurate.Validator
import dev.nesk.akkurate.constraints.builders.isNotEqualTo
import dev.nesk.akkurate.constraints.constrain
import dev.nesk.akkurate.constraints.otherwise
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

val validateOrderCreate = Validator.suspendable<OrderCreate> {
    fromStation.isNotEqualTo(toStation)

    fromStation.constrain {
        suspendTransaction {
            StationDAO.findById(fromStation.unwrap())
        } != null
    }.otherwise { "Station ${fromStation.unwrap()} does not exist" }

    toStation.constrain {
        suspendTransaction {
            StationDAO.findById(toStation.unwrap())
        } != null
    }.otherwise { "Station ${toStation.unwrap()} does not exist" }
}

fun Application.ordersValidation() {
    install(RequestValidation) {
        validate<OrderCreate> {
            validateModel(it, validateOrderCreate)
        }
    }
}
