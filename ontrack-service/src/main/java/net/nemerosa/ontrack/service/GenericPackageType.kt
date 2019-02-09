package net.nemerosa.ontrack.service

import net.nemerosa.ontrack.model.structure.PackageType
import org.springframework.stereotype.Component

@Component
class GenericPackageType : PackageType {
    override val name: String = "Generic"
    override val description: String = "Simple package identified by some free text"
}
