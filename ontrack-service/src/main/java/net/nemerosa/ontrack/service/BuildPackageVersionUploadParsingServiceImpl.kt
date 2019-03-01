package net.nemerosa.ontrack.service

import net.nemerosa.ontrack.common.Document
import net.nemerosa.ontrack.model.exceptions.BuildPackageVersionUploadWrongMimeTypeException
import net.nemerosa.ontrack.model.structure.BuildPackageVersionParser
import net.nemerosa.ontrack.model.structure.BuildPackageVersionUploadParsingService
import net.nemerosa.ontrack.model.structure.PackageType
import net.nemerosa.ontrack.model.structure.PackageVersion
import org.springframework.stereotype.Service

@Service
class BuildPackageVersionUploadParsingServiceImpl(
        private val parsers: List<BuildPackageVersionParser>
) : BuildPackageVersionUploadParsingService {

    override fun parsePackageVersions(defaultType: PackageType, doc: Document): List<PackageVersion> {
        if (doc.isEmpty) {
            return emptyList()
        } else {
            val mimeType: String = doc.type
            val parser = parsers.find {
                it.mimeType == mimeType
            } ?: throw BuildPackageVersionUploadWrongMimeTypeException(mimeType)
            return parser.parsePackageVersions(defaultType, doc)
        }
    }

}