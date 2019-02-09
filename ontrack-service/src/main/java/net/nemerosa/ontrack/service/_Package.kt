package net.nemerosa.ontrack.service

import net.nemerosa.ontrack.model.structure.PackageType

/**
 * ID of a [PackageType]
 */
val PackageType.id: String get() = this::class.java.name
