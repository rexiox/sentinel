package sentinel.attest.result

import sentinel.attest.exception.AttestationErrorCode

sealed class AttestationResult {

    data class Error(
        val cause: Throwable,
        val code: AttestationErrorCode,
    ) : AttestationResult()
}