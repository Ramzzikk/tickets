package com.tickets.stations

import com.tickets.db.dao.StationDAO
import com.tickets.models.Station
import com.tickets.utils.suspendTransaction
import io.github.smiley4.ktorswaggerui.dsl.get
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.stationsRoutes() {
    routing {
        authenticate("JWT") {
            get("stations", {
                description = "Получение списка станций"
                response {
                    HttpStatusCode.OK to {
                        description = "Успешно"
                        body<List<Station>>()
                    }
                }
            }) {
                val stations = suspendTransaction {
                    StationDAO.all().map { it.toStation() }
                }

                call.respond(stations)
            }
        }
    }
}
