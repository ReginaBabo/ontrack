package net.nemerosa.ontrack.boot.ui

import net.nemerosa.ontrack.model.Ack
import net.nemerosa.ontrack.model.form.Form
import net.nemerosa.ontrack.model.form.MultiForm
import net.nemerosa.ontrack.model.form.Selection
import net.nemerosa.ontrack.model.form.Text
import net.nemerosa.ontrack.model.structure.*
import net.nemerosa.ontrack.ui.controller.AbstractResourceController
import net.nemerosa.ontrack.ui.resource.Resources
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on

@RestController
@RequestMapping("/rest/packages/projects")
class ProjectPackageController(
        private val structureService: StructureService,
        private val projectPackageService: ProjectPackageService,
        private val packageService: PackageService
) : AbstractResourceController() {

    @GetMapping("{projectId}")
    fun getPackageIdsForProject(@PathVariable projectId: ID): Resources<PackageId> {
        return Resources.of(
                projectPackageService.getPackagesForProject(
                        structureService.getProject(projectId)
                ),
                uri(on(javaClass).getPackageIdsForProject(projectId))
        )
    }

    @GetMapping("{projectId}/edit")
    fun getPackageIdsFormForProject(@PathVariable projectId: ID): Form {
        // List of existing packages for the project
        val packages = projectPackageService.getPackagesForProject(
                structureService.getProject(projectId)
        )
        // List of existing types
        val types = packageService.packageTypes
        // Form
        return Form.create()
                .with(
                        MultiForm.of(
                                "packages",
                                Form.create()
                                        .with(
                                                Selection.of("type")
                                                        .label("Package type")
                                                        .items(types)
                                        )
                                        .with(Text.of("id").label("Package ID"))
                        ).label("Package IDs")
                                .value(packages)

                )
    }

    @PutMapping("{projectId}/edit")
    fun setPackageIdsForProject(@PathVariable projectId: ID, @RequestBody input: PackageIdsForm): Ack {
        val packages = input.packages.mapNotNull { i ->
            packageService.findByNameOrId(i.type)
                    ?.let {
                        PackageId(
                                type = it,
                                id = i.id
                        )
                    }
        }
        projectPackageService.setPackagesForProject(
                structureService.getProject(projectId),
                packages
        )
        return Ack.OK
    }


}