package com.rs.attest

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UByteVar
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.set
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreCrypto.CC_SHA256
import platform.CoreCrypto.CC_SHA256_DIGEST_LENGTH
import platform.DeviceCheck.DCAppAttestService
import platform.Foundation.NSData
import platform.Foundation.base64Encoding
import platform.Foundation.dataWithBytes
import sentinel.attest.provider.AttestProvider
import sentinel.attest.exception.AttestationException
import sentinel.attest.exception.AttestationErrorCode
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AppAttestProvider : AttestProvider {

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun getToken(nonce: String): String = suspendCancellableCoroutine { cont ->
        val service = DCAppAttestService.sharedService

        if (!service.supported) {
            cont.resumeWithException(
                AttestationException(
                    message = "App Attest is not supported.",
                    code = AttestationErrorCode.NOT_SUPPORTED
                )
            )
            return@suspendCancellableCoroutine
        }

        service.generateKeyWithCompletionHandler { keyId, error ->
            if (error != null || keyId == null) {
                cont.resumeWithException(
                    AttestationException(
                        message = "Key could not be generated: ${error?.localizedDescription}",
                        code = AttestationErrorCode.ATTESTATION_FAILED
                    )
                )
                return@generateKeyWithCompletionHandler
            }

            val hash = nonce.encodeToByteArray().sha256()

            service.attestKey(keyId = keyId, clientDataHash = hash) { attestation, attestError ->
                when {
                    attestError != null -> cont.resumeWithException(
                        AttestationException(
                            message = "Attestation failure: ${attestError.localizedDescription}",
                            code = AttestationErrorCode.ATTESTATION_FAILED
                        )
                    )

                    attestation == null -> cont.resumeWithException(
                        AttestationException(
                            message = "Attestation value empty.",
                            code = AttestationErrorCode.ATTESTATION_FAILED
                        )
                    )

                    else -> cont.resume(attestation.base64Encoding())
                }
            }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun ByteArray.sha256(): NSData = memScoped {
        val input  = allocArray<ByteVar>(size)
        val digest = allocArray<UByteVar>(CC_SHA256_DIGEST_LENGTH)
        forEachIndexed { i, b -> input[i] = b }
        CC_SHA256(input, size.toUInt(), digest)
        NSData.dataWithBytes(digest, CC_SHA256_DIGEST_LENGTH.toULong())
    }

    @OptIn(ExperimentalForeignApi::class)
    fun ByteArray.toNSData(): NSData = memScoped {
        val buf = allocArray<ByteVar>(size)
        forEachIndexed { i, b -> buf[i] = b }
        NSData.dataWithBytes(buf, size.toULong())
    }
}