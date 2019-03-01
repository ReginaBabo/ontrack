package net.nemerosa.ontrack.model.structure

import net.nemerosa.ontrack.common.Document

/**
 * Parsing of document containing a list of package versions to be
 * used by [BuildPackageVersionUploadService].
 */
interface BuildPackageVersionUploadParsingService {

    companion object {
        /**
         * Expected MIME type
         */
        const val UPLOAD_TYPE: String = "application/toml"
    }

    /**
     * Parsing of the document.
     */
    fun parsePackageVersions(defaultType: PackageType, doc: Document): List<PackageVersion>

}