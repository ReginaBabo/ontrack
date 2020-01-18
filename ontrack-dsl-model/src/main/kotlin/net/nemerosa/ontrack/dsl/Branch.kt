package net.nemerosa.ontrack.dsl

interface Branch : ProjectEntity {
    val name: String
    val project: Project
}
