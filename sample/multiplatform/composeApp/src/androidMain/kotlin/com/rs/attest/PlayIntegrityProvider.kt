package com.rs.attest

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.android.play.core.integrity.IntegrityManagerFactory
import com.google.android.play.core.integrity.IntegrityTokenRequest
import com.google.android.play.core.integrity.IntegrityTokenResponse
import kotlinx.coroutines.suspendCancellableCoroutine
import sentinel.attest.exception.AttestationErrorCode
import sentinel.attest.exception.AttestationException
import sentinel.attest.provider.AttestProvider
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class PlayIntegrityProvider(context: Context) : AttestProvider {

    private val integrityManager = IntegrityManagerFactory.create(context)

    override suspend fun getToken(nonce: String): String {
        val integrityTokenResponse: Task<IntegrityTokenResponse> =
            integrityManager.requestIntegrityToken(
                IntegrityTokenRequest.builder()
                    .setNonce(nonce)
                    .build()
            )

        return suspendCancellableCoroutine { cont ->
            integrityTokenResponse
                .addOnSuccessListener {
                    cont.resume(it.token())
                }
                .addOnFailureListener { e ->
                    cont.resumeWithException(
                        AttestationException(
                            message = "Attestation failure: ${e.localizedMessage}",
                            code = AttestationErrorCode.ATTESTATION_FAILED
                        )
                    )
                }
        }
    }
}