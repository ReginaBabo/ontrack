package net.nemerosa.ontrack.kdsl.model.admin

import net.nemerosa.ontrack.kdsl.model.Entity

interface Account : Entity {
    val name: String
    val fullName: String
    val email: String
}