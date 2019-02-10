package net.nemerosa.ontrack.extension.api

import net.nemerosa.ontrack.model.extension.Extension

interface PackageTypeExtension : Extension {

    val name: String
    val description: String

    val id: String get() = this::class.java.name

    val descriptor: PackageTypeDescription
        get() = PackageTypeDescription(
                id,
                name,
                description
        )

}