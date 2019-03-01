package net.nemerosa.ontrack.boot.ui

import net.nemerosa.ontrack.common.Document
import net.nemerosa.ontrack.model.Ack
import net.nemerosa.ontrack.model.structure.*
import net.nemerosa.ontrack.ui.controller.AbstractResourceController
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

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
    fun uploadPackageVersions(@PathVariable buildId: ID, @RequestParam file: MultipartFile): Ack {
        val document = Document(
                file.contentType,
                file.bytes
        )
        // Gets the build
        val build = structureService.getBuild(buildId)
        // Gets the default package type
        val defaultType = packageService.defaultPackageType
        // Parsing of the document
        val packageVersions = buildPackageVersionUploadParsingService.parsePackageVersions(defaultType, document)
        // Uploads of versions
        buildPackageVersionUploadService.uploadAndResolvePackageVersions(build, packageVersions)
        // OK
        return Ack.OK
    }
}