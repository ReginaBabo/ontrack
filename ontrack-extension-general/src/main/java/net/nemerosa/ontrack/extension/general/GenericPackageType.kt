package net.nemerosa.ontrack.extension.general

import net.nemerosa.ontrack.extension.support.AbstractExtension
import net.nemerosa.ontrack.model.structure.PackageType
import org.springframework.stereotype.Component

@Component
class GenericPackageType(
        extensionFeature: GeneralExtensionFeature
) : AbstractExtension(extensionFeature), PackageType {
    override val name: String = "Generic"
    override val description: String = "Simple package identified by some free text"
}
