package net.nemerosa.ontrack.extension.general

import net.nemerosa.ontrack.extension.support.AbstractExtension
import net.nemerosa.ontrack.model.structure.PackageType
import org.springframework.stereotype.Component

@Component
class MavenPackageType(
        extensionFeature: GeneralExtensionFeature
) : AbstractExtension(extensionFeature), PackageType {
    override val name: String = "Maven"
    override val description: String = "Package identified based on Maven group and artifact ID"
}
