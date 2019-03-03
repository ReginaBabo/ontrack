package net.nemerosa.ontrack.boot.ui

import net.nemerosa.ontrack.common.Document
import net.nemerosa.ontrack.model.exceptions.PackageTypeNotFoundException
import net.nemerosa.ontrack.model.structure.*
import net.nemerosa.ontrack.ui.controller.AbstractResourceController
import net.nemerosa.ontrack.ui.resource.Resources
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on

@RestController
@RequestMapping("/rest/builds")
class BuildPackageVersionUploadController(
        private val structureService: StructureService,
        private val buildPackageVersionUploadParsingService: BuildPackageVersionUploadParsingService,
        private val buildPackageVersionUploadService: BuildPackageVersionUploadService,
        private val packageService: PackageService
) : AbstractResourceController() {

    @PostMapping("{buildId}/packages/upload")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun uploadPackageVersions(
            @PathVariable buildId: ID,
            @RequestParam(required = false)
            defaultTypeName: String?,
            @RequestParam file: MultipartFile
    ): Resources<BuildPackageVersion> {
        val document = Document(
                file.contentType,
                file.bytes
        )
        // Gets the build
        val build = structureService.getBuild(buildId)
        // Gets the default package type
        val defaultType = if (defaultTypeName != null && defaultTypeName.isNotBlank()) {
            packageService.findByNameOrId(defaultTypeName)
                    ?: throw PackageTypeNotFoundException(defaultTypeName)
        } else {
            packageService.defaultPackageType
        }
        // Parsing of the document
        val packageVersions = buildPackageVersionUploadParsingService.parsePackageVersions(defaultType, document)
        // Uploads of versions
        val versions = buildPackageVersionUploadService.uploadAndResolvePackageVersions(build, packageVersions)
        // OK
        return Resources.of(
                versions,
                uri(on(BuildPackageVersionController::class.java).getPackageVersions(buildId))
        )
    }
}