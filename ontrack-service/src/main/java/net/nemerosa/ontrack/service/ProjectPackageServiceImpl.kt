package net.nemerosa.ontrack.service

import net.nemerosa.ontrack.model.security.ProjectEdit
import net.nemerosa.ontrack.model.security.ProjectView
import net.nemerosa.ontrack.model.security.SecurityService
import net.nemerosa.ontrack.model.structure.*
import net.nemerosa.ontrack.repository.ProjectPackageRepository
import net.nemerosa.ontrack.repository.TProjectPackage
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProjectPackageServiceImpl(
        private val structureService: StructureService,
        private val securityService: SecurityService,
        private val packageService: PackageService,
        private val projectPackageRepository: ProjectPackageRepository
) : ProjectPackageService {
    override fun findProjectsWithPackageIdentifier(id: PackageId): List<Project> {
        return projectPackageRepository.findProjectsWithPackageIdentifier(id.type.id, id.id)
                .filter { securityService.isProjectFunctionGranted(it, ProjectView::class.java) }
                .map { structureService.getProject(ID.of(it)) }
    }

    override fun getPackagesForProject(project: Project): List<PackageId> {
        securityService.checkProjectFunction(project, ProjectView::class.java)
        return projectPackageRepository.getPackagesForProject(project.id())
                .mapNotNull { t ->
                    packageService.getPackageType(t.type)?.let { p ->
                        PackageId(p, t.id)
                    }
                }
    }

    override fun setPackagesForProject(project: Project, packages: List<PackageId>) {
        // FIXME #650 Project function for edition of packages id, for project manager role
        securityService.checkProjectFunction(project, ProjectEdit::class.java)
        projectPackageRepository.setPackagesForProject(
                project.id(),
                packages.map {
                    TProjectPackage(it.type.id, it.id)
                }
        )
    }
}