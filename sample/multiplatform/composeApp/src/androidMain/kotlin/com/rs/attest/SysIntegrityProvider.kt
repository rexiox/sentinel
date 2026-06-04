package com.rs.attest

import android.content.Context
import com.huawei.hms.support.api.entity.safetydetect.SysIntegrityRequest
import com.huawei.hms.support.api.safetydetect.SafetyDetect
import kotlinx.coroutines.suspendCancellableCoroutine
import sentinel.attest.exception.AttestationErrorCode
import sentinel.attest.exception.AttestationException
import sentinel.attest.provider.AttestProvider
import java.nio.charset.StandardCharsets
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class SysIntegrityProvider(
    private val context: Context,
    private val appId: String,
) : AttestProvider {

    override suspend fun getToken(nonce: String): String {
        val nonceBytes = nonce.toByteArray(StandardCharsets.UTF_8)

        val sysIntegrityRequest = SysIntegrityRequest().apply {
            this.appId = this@SysIntegrityProvider.appId
            this.nonce = nonceBytes
            this.alg = "PS256"
        }

        return suspendCancellableCoroutine { continuation ->
            SafetyDetect.getClient(context)
                .sysIntegrity(sysIntegrityRequest)
                .addOnSuccessListener { response ->
                    val jwsStr = response.result

                    if (jwsStr.isNotBlank()) {
                        continuation.resume(jwsStr)
                    } else {
                        continuation.resumeWithException(
                            AttestationException(
                                message = "Attestation failure: SysIntegrity response result is null or empty",
                                code = AttestationErrorCode.ATTESTATION_FAILED
                            )
                        )
                    }
                }
                .addOnFailureListener { e ->
                    continuation.resumeWithException(
                        AttestationException(
                            message = "Attestation failure: ${e.localizedMessage}",
                            code = AttestationErrorCode.ATTESTATION_FAILED
                        )
                    )
                }
        }
    }
}