package com.rs.data.service

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import sentinel.attest.result.EncryptedAttestation

internal suspend fun fetchNonce(): String {
    val client = HttpClient()

    return try {
        val httpResponse = client.get("https://example.com/api/v1/attestation/nonce")
        val jsonString = httpResponse.bodyAsText()

        val jsonElement = Json.parseToJsonElement(jsonString)
        val title = jsonElement.jsonObject["nonce"]?.jsonPrimitive?.content

        title.orEmpty()
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    } finally {
        client.close()
    }
}

internal suspend fun fetchVerify(attestation: EncryptedAttestation): String? {
    val client = HttpClient()

    return try {
        val jsonBody = Json.encodeToString(attestation)
        val response = client.post("https://example.com/api/v1/attestation/verify") {
            contentType(ContentType.Application.Json)
            setBody(jsonBody)
        }

        response.bodyAsText()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    } finally {
        client.close()
    }
}