package net.nemerosa.ontrack.kdsl.model

import java.time.LocalDateTime

data class Signature(
        val time: LocalDateTime,
        val user: String
)
