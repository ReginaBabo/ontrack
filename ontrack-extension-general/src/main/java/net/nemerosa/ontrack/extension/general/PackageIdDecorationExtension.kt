package net.nemerosa.ontrack.extension.general

import net.nemerosa.ontrack.extension.api.DecorationExtension
import net.nemerosa.ontrack.extension.support.AbstractExtension
import net.nemerosa.ontrack.model.structure.*
import org.springframework.stereotype.Component
import java.util.*

@Component
class PackageIdDecorationExtension(
        extensionFeature: GeneralExtensionFeature,
        private val projectPackageService: ProjectPackageService
) : AbstractExtension(extensionFeature), DecorationExtension<String> {

    override fun getDecorations(entity: ProjectEntity): List<Decoration<String>> {
        return if (entity is Project) {
            val packageIds = projectPackageService.getPackagesForProject(entity)
            if (packageIds.isEmpty()) {
                emptyList()
            } else {
                val summary = packageIds
                        .map { it.type.name }
                        .distinct()
                        .joinToString(", ")
                listOf(
                        Decoration.of(
                                this,
                                summary
                        )
                )
            }
        } else {
            emptyList()
        }
    }

    override fun getScope(): EnumSet<ProjectEntityType> =
            EnumSet.of(ProjectEntityType.PROJECT)
}