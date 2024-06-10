package com.tickets.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object UsersTable : IntIdTable(name = "users") {
    val nickname = varchar("nickname", 255)
    val email = varchar("email", 255).uniqueIndex()
    val password = varchar("password", 255)
    val created = datetime("created").defaultExpression(CurrentDateTime)
}