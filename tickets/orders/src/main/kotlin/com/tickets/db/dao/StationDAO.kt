package com.tickets.db.dao

import com.tickets.db.tables.StationsTable
import com.tickets.models.Station
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class StationDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<StationDAO>(StationsTable)

    var station by StationsTable.station

    fun toStation() = Station(
        id.value,
        station
    )
}