package com.tickets.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

class JwtHelper(secretKey: String, private val audience: String, private val issuer: String) {
    private val algorithm: Algorithm = Algorithm.HMAC256(secretKey)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .withAudience(audience)
        .build()

    private fun createToken(subject: String, expirationMillis: Long = 24 * 60 * 60 * 1000): JwtData {
        val now = System.currentTimeMillis()
        val expiration = Date(now + expirationMillis)

        val token = JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withSubject(subject)
            .withIssuedAt(Date(now))
            .withNotBefore(Date(now))
            .withExpiresAt(expiration)
            .sign(algorithm)

        return JwtData(token, expiration.time)
    }

    fun createToken(subject: Int, expirationMillis: Long = 24 * 60 * 60 * 1000) =
        createToken(subject.toString(), expirationMillis)
}
