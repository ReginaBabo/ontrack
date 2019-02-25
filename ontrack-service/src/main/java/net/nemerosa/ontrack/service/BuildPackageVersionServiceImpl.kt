package net.nemerosa.ontrack.service

import net.nemerosa.ontrack.model.structure.*
import net.nemerosa.ontrack.repository.BuildPackageRepository
import net.nemerosa.ontrack.repository.TBuildPackageVersion
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BuildPackageVersionServiceImpl(
        private val structureService: StructureService,
        private val packageService: PackageService,
        private val buildPackageRepository: BuildPackageRepository
) : BuildPackageVersionService {

    override fun saveBuildPackage(parent: Build, buildPackageVersion: BuildPackageVersion) {
        buildPackageRepository.saveBuildPackage(
                TBuildPackageVersion(parent, buildPackageVersion)
        )
    }

    override fun getBuildPackages(parent: Build): List<BuildPackageVersion> {
        return buildPackageRepository
                .getBuildPackages(parent)
                .mapNotNull { record ->
                    record.toBuildPackageVersion()
                }
    }

    override fun onUnassignedPackages(project: Project, code: (Build, BuildPackageVersion) -> Unit) {
        buildPackageRepository.onUnassignedPackages(project.id()) { record ->
            val version = record.toBuildPackageVersion()
            if (version != null) {
                val build = structureService.getBuild(ID.of(record.parent))
                code(build, version)
            }
        }
    }

    private fun TBuildPackageVersion.toBuildPackageVersion() =
            packageService.getPackageType(type)?.let { type ->
                BuildPackageVersion(
                        packageVersion = type.toId(id).toVersion(version),
                        target = target?.let {
                            structureService.getBuild(ID.of(it))
                        }
                )
            }
}