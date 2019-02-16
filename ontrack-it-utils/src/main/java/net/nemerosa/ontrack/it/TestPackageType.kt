package net.nemerosa.ontrack.it

import net.nemerosa.ontrack.extension.api.support.TestExtensionFeature
import net.nemerosa.ontrack.extension.support.AbstractExtension
import net.nemerosa.ontrack.model.structure.PackageType
import org.springframework.stereotype.Component

@Component
class TestPackageType(extensionFeature: TestExtensionFeature) : AbstractExtension(extensionFeature), PackageType {
    override val name: String = "Test"
    override val description: String = "Test package"
}