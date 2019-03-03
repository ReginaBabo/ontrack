package net.nemerosa.ontrack.service

import net.nemerosa.ontrack.model.security.BuildConfig
import net.nemerosa.ontrack.model.security.SecurityService
import net.nemerosa.ontrack.model.security.callAsAdmin
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

    override fun uploadAndResolvePackageVersions(parent: Build, packages: List<PackageVersion>): List<BuildPackageVersion> {
        securityService.checkProjectFunction(parent, BuildConfig::class.java)
        buildPackageVersionService.clearBuildPackages(parent)
        // Other projects might not be visible by current user, so running as admin
        return securityService.callAsAdmin {
            packages.map {
                uploadAndResolvePackageVersion(parent, it)
            }
        }
    }

    private fun uploadAndResolvePackageVersion(parent: Build, packageVersion: PackageVersion): BuildPackageVersion {
        val target = findBuildByPackageVersion(packageVersion)
        // Link?
        if (target != null) {
            structureService.addBuildLink(parent, target)
        }
        // Saves the package version
        val buildPackageVersion = BuildPackageVersion(packageVersion, target)
        buildPackageVersionService.saveBuildPackage(parent, buildPackageVersion)
        return buildPackageVersion
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
        }.sortedByDescending { it.signature.time }
        // First one
        return builds.firstOrNull()
    }

}