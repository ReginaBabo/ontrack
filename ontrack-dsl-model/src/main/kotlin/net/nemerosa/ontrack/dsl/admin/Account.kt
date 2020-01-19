package net.nemerosa.ontrack.dsl.admin

import net.nemerosa.ontrack.dsl.Entity

interface Account : Entity {
    val name: String
    val fullName: String
    val email: String
}