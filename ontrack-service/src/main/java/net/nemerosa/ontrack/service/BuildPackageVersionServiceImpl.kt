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

    override fun getUnassignedPackages(): List<BuildPackageVersionLink> {
        val unassignedPackages = buildPackageRepository.getUnassignedPackages()
        return unassignedPackages
                // Grouping by parent ID
                .groupBy { record -> record.parent }
                // Transforms records in actual package lines
                .mapValues { (_, records) ->
                    records.mapNotNull { record ->
                        record.toBuildPackageVersion()
                    }
                }
                // Filters out the empty collections
                .filter { (_, records) ->
                    !records.isEmpty()
                }
                // Loading the parent build
                .map { (id, packages) ->
                    BuildPackageVersionLink(
                            parent = structureService.getBuild(ID.of(id)),
                            packages = packages
                    )
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