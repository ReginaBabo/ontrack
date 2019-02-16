package net.nemerosa.ontrack.extension.general

import net.nemerosa.ontrack.extension.support.AbstractExtension
import net.nemerosa.ontrack.model.structure.PackageType
import org.springframework.stereotype.Component

@Component
class NpmPackageType(
        extensionFeature: GeneralExtensionFeature
) : AbstractExtension(extensionFeature), PackageType {
    override val name: String = "Npm"
    override val description: String = "Package identified based on Npm package name"
}
