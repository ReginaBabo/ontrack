package net.nemerosa.ontrack.model.structure

import net.nemerosa.ontrack.common.Document

/**
 * Parser for a list of versions.
 */
interface BuildPackageVersionParser {

    /**
     * Supported MIME type
     */
    val mimeType: String

    /**
     * Parsing of the document.
     */
    fun parsePackageVersions(defaultType: PackageType, doc: Document): List<PackageVersion>

}