package net.nemerosa.ontrack.service

import net.nemerosa.ontrack.model.security.BuildConfig
import net.nemerosa.ontrack.model.security.SecurityService
import net.nemerosa.ontrack.model.structure.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BuildPackageVersionUploadServiceImpl(
        private val structureService: StructureService,
        private val securityService: SecurityService,
        private val buildPackageVersionService: BuildPackageVersionService,
        private val projectPackageService: ProjectPackageService,
        private val contributors: List<ProjectBuildSearchContributor>
) : BuildPackageVersionUploadService {

    override fun uploadAndResolvePackageVersions(parent: Build, packages: List<PackageVersion>) {
        securityService.checkProjectFunction(parent, BuildConfig::class.java)
        buildPackageVersionService.clearBuildPackages(parent)
        // Other projects might not be visible by current user, so running as admin
        securityService.asAdmin {
            packages.forEach {
                uploadAndResolvePackageVersion(parent, it)
            }
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
            // Gets the name search form for this project
            val searchForm = ProjectBuildSearchContributorUtils.getSearchForm(
                    project,
                    packageVersion.version,
                    contributors
            ).withMaximumCount(1)
            structureService.buildSearch(
                    project.id,
                    searchForm
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