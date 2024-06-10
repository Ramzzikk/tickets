package com.tickets.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object StationsTable : IntIdTable(name = "stations") {
    val station = varchar("station", 255)
}