package net.nemerosa.ontrack.model.structure

interface PackageType {

    val name: String
    val description: String

    val id: String get() = this::class.java.name

}