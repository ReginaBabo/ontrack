package net.nemerosa.ontrack.service

import net.nemerosa.ontrack.model.structure.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BuildPackageVersionUploadServiceImpl(
        private val structureService: StructureService,
        private val buildPackageVersionService: BuildPackageVersionService,
        private val projectPackageService: ProjectPackageService
) : BuildPackageVersionUploadService {

    override fun uploadAndResolvePackageVersions(parent: Build, packages: List<PackageVersion>) {
        // TODO Clears previous versions
        packages.forEach {
            uploadAndResolvePackageVersion(parent, it)
        }
    }

    private fun uploadAndResolvePackageVersion(parent: Build, packageVersion: PackageVersion) {
        val target = findBuildByPackageVersion(packageVersion)
        // Link?
        if (target != null) {
            structureService.addBuildLink(parent, target)
        }
        // Saves the package version
        buildPackageVersionService.saveBuildPackage(parent, BuildPackageVersion(packageVersion, target))
    }

    private fun findBuildByPackageVersion(packageVersion: PackageVersion): Build? {
        val packageId = packageVersion.packageId
        // Gets the list of projects designed by this package ID
        val projects = projectPackageService.findProjectsWithPackageIdentifier(packageId)
        // Gets the list of matching builds
        val builds = projects.flatMap { project ->
            structureService.buildSearch(
                    project.id,
                    BuildSearchForm().withBuildExactMatch(true)
                            .withMaximumCount(1)
                            // TODO Use label when needed
                            .withBuildName(packageVersion.version)
            )
        }
        // OK it only ONE build
        return if (builds.size == 1) {
            builds[0]
        } else {
            null
        }
    }

}