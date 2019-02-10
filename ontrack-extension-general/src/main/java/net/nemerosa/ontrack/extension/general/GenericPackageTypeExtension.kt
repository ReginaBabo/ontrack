package net.nemerosa.ontrack.extension.general

import net.nemerosa.ontrack.extension.api.PackageTypeExtension
import net.nemerosa.ontrack.extension.support.AbstractExtension
import org.springframework.stereotype.Component

@Component
class GenericPackageTypeExtension(
        extensionFeature: GeneralExtensionFeature
) : AbstractExtension(extensionFeature), PackageTypeExtension {

    override val name: String = "Generic"
    override val description: String = "Simple package identified by some free text"

}
