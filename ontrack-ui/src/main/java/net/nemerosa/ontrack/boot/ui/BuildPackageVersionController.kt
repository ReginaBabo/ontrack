package net.nemerosa.ontrack.boot.ui

import net.nemerosa.ontrack.model.structure.BuildPackageVersion
import net.nemerosa.ontrack.model.structure.BuildPackageVersionService
import net.nemerosa.ontrack.model.structure.ID
import net.nemerosa.ontrack.model.structure.StructureService
import net.nemerosa.ontrack.ui.controller.AbstractResourceController
import net.nemerosa.ontrack.ui.resource.Resources
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on

@RestController
@RequestMapping("/rest/builds")
class BuildPackageVersionController(
        private val structureService: StructureService,
        private val buildPackageVersionService: BuildPackageVersionService
) : AbstractResourceController() {

    @GetMapping("{buildId}/packages")
    fun getPackageVersions(@PathVariable buildId: ID): Resources<BuildPackageVersion> {
        return Resources.of(
                buildPackageVersionService.getBuildPackages(
                        structureService.getBuild(buildId)
                ),
                uri(on(javaClass).getPackageVersions(buildId))
        )
    }
}
