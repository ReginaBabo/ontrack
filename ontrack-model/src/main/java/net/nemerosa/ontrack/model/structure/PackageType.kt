package net.nemerosa.ontrack.model.structure

import net.nemerosa.ontrack.model.extension.Extension

interface PackageType : Extension {

    val name: String
    val description: String

    val id: String get() = this::class.java.name

}