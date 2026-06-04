package com.rs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.rs.sentinel.resources.Res
import co.rs.sentinel.resources.ic_logo
import com.rs.Key.RSA_PUBLIC_KEY
import org.jetbrains.compose.resources.painterResource
import sentinel.Sentinel
import sentinel.attest.SentinelAttest
import sentinel.attest.provider.AttestProvider
import sentinel.core.logger.SentinelLogger.print
import sentinel.crypto.generator.generateSecureNonce

@Composable
internal fun Splash(
    sentinel: Sentinel,
    attestProvider: AttestProvider,
    onCompleted: () -> Unit,
) {
    val sentinelAttest = remember(attestProvider) {
        SentinelAttest.configure {
            provider = attestProvider

            publicKey = RSA_PUBLIC_KEY

            nonce {
                // fetchNonce()
                generateSecureNonce()
            }

            callbacks {
                verify { encryptedAttestation ->
                    // fetchVerify(attestation = encryptedAttestation)
                }

                onError { error ->
                    print(msg = error)
                }

                onComplete {
                    onCompleted()
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        sentinelAttest.execute(report = sentinel.inspect())
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.size(size = 175.dp),
            painter = painterResource(resource = Res.drawable.ic_logo),
            contentDescription = "logo"
        )
    }
}