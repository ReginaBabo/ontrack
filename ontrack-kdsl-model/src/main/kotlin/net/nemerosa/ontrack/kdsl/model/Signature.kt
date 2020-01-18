package net.nemerosa.ontrack.kdsl.model

import java.time.LocalDateTime

/**
 * Signature, or association between a user name and
 * a timestamp.
 *
 * @property time Timestamp of the signature (always UTC)
 * @property user User
 */
data class Signature(
        val time: LocalDateTime,
        val user: User
)
