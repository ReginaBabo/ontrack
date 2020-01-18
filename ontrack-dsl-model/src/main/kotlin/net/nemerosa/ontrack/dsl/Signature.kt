package net.nemerosa.ontrack.dsl

import java.time.LocalDateTime

data class Signature(
        val time: LocalDateTime,
        val user: String
)
