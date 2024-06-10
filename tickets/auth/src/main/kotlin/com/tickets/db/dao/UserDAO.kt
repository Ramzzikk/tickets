package com.tickets.db.dao

import com.tickets.db.tables.UsersTable
import com.tickets.models.user.User
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserDAO>(UsersTable)

    var nickname by UsersTable.nickname
    var email by UsersTable.email
    var password by UsersTable.password
    private var created by UsersTable.created

    fun toUser() = User(
        id.value,
        nickname,
        email,
        created.toString()
    )
}