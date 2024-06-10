package com.tickets.plugins

import com.tickets.db.tables.UsersTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun configureDatabases() {
    Database.connect(
        url = "jdbc:postgresql://auth-db:5432/auth",
        user = "postgres",
    )

    transaction {
        SchemaUtils.create(UsersTable)
    }
}
