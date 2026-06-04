package sentinel.attest.exception

class AttestationException(
    val code: AttestationErrorCode,
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause)