package com.tickets

import com.tickets.orders.ordersRoutes
import com.tickets.orders.ordersValidation
import com.tickets.plugins.configureDatabases
import com.tickets.plugins.configureSecurity
import com.tickets.plugins.configureStatusPages
import com.tickets.plugins.configureSwagger
import com.tickets.stations.stationsRoutes
import com.tickets.utils.getJson
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.doublereceive.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(ContentNegotiation) {
        json(getJson())
    }
    install(DoubleReceive)

    configureSecurity()
    configureDatabases()
    configureStatusPages()

    if (environment.developmentMode) {
        configureSwagger()
    }

    stationsRoutes()

    ordersRoutes()
    ordersValidation()
}
