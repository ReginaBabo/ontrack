package net.nemerosa.ontrack.repository

import net.nemerosa.ontrack.common.Time
import net.nemerosa.ontrack.model.structure.Build
import net.nemerosa.ontrack.model.structure.BuildPackageVersion
import java.time.LocalDateTime

class TBuildPackageVersion(
        val parent: Int,
        val type: String,
        val id: String,
        val version: String,
        val creation: LocalDateTime,
        val target: Int?
) {
    constructor(parent: Build, buildPackageVersion: BuildPackageVersion) : this(
            parent = parent.id(),
            type = buildPackageVersion.packageVersion.packageId.type.id,
            id = buildPackageVersion.packageVersion.packageId.id,
            version = buildPackageVersion.packageVersion.version,
            creation = Time.now(),
            target = buildPackageVersion.target?.id()
    )
}