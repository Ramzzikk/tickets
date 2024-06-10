package com.tickets.orders

import com.tickets.db.dao.OrderDAO
import com.tickets.db.tables.OrdersTable
import com.tickets.db.tables.StationsTable
import com.tickets.models.User
import com.tickets.models.order.Order
import com.tickets.models.order.OrderCreate
import com.tickets.models.response.ErrorResponse
import com.tickets.models.response.ValidationResponse
import com.tickets.utils.getJson
import com.tickets.utils.suspendTransaction
import io.github.smiley4.ktorswaggerui.dsl.get
import io.github.smiley4.ktorswaggerui.dsl.post
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.and

fun Application.ordersRoutes() {
    routing {
        authenticate("JWT") {
            route("orders") {
                get("/{id}", {
                    description = "Получение заказа по идентификатору"
                    request {
                        pathParameter<Int>("id") {
                            required = true
                            description = "ID заказа"
                        }
                    }
                    response {
                        HttpStatusCode.OK to {
                            description = "Успешно"
                            body<Order>()
                        }

                        HttpStatusCode.NotFound to {
                            description = "Заказ не найден"
                            body<ErrorResponse>()
                        }
                    }
                }) {
                    val principal = call.principal<User>()!!

                    val order = suspendTransaction {
                        OrderDAO.find { (OrdersTable.id eq call.parameters["id"]!!.toInt()) and (OrdersTable.userId eq principal.id) }.firstOrNull()
                    }?.toOrder()

                    if (order != null) {
                        call.respond(order)
                    } else {
                        call.respond(HttpStatusCode.NotFound, ErrorResponse("Order not found"))
                    }
                }

                get({
                    description = "Получение списка заказов"
                    response {
                        HttpStatusCode.OK to {
                            description = "Успешно"
                            body<List<Order>>()
                        }
                    }
                }) {
                    val principal = call.principal<User>()!!

                    val orders = suspendTransaction {
                        OrderDAO.find { OrdersTable.userId eq principal.id }.map { it.toOrder() }
                    }

                    call.respond(orders)
                }

                post({
                    description = "Создание заказа"
                    request {
                        body<OrderCreate> {
                            required = true
                        }
                    }
                    response {
                        HttpStatusCode.Created to {
                            description = "Успешно"
                            body<Order>()
                        }

                        HttpStatusCode.UnprocessableEntity to {
                            description = "Неверный формат данных"
                            body<ValidationResponse>()
                        }
                    }
                }) {
                    val principal = call.principal<User>()!!

                    val order = try {
                        call.receive<OrderCreate>()
                    } catch (e: BadRequestException) {
                        getJson().decodeFromString<OrderCreate>(call.receiveText())
                        return@post
                    }

                    val newOrder = suspendTransaction {
                        OrderDAO.new {
                            userId = principal.id
                            fromStationId = EntityID(order.fromStation, StationsTable)
                            toStationId = EntityID(order.toStation, StationsTable)
                        }
                    }

                    call.respond(HttpStatusCode.Created, newOrder.toOrder())
                }
            }
        }
    }
}