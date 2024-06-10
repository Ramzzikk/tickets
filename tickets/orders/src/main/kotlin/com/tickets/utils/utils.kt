package com.tickets.utils

import dev.nesk.akkurate.ValidationResult.Failure
import dev.nesk.akkurate.ValidationResult.Success
import dev.nesk.akkurate.Validator
import io.ktor.server.plugins.requestvalidation.*
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> validateModel(it: T, validator: Validator.SuspendableRunner<T>) = when (val result = validator(it)) {
    is Success -> ValidationResult.Valid
    is Failure -> {
        val reasons = result.violations.map {
            "${it.path.joinToString(".")}: ${it.message}"
        }
        ValidationResult.Invalid(reasons)
    }
}

fun getJson() = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
    isLenient = true
}

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)
