package net.nemerosa.ontrack.model.structure

import net.nemerosa.ontrack.common.Document

/**
 * Parser for a list of versions.
 */
interface BuildPackageVersionParser {

    /**
     * Name of this parser
     */
    val name: String

    /**
     * Description for this parser
     */
    val description: String

    /**
     * Preferred MIME type
     */
    val mimeType: String

    /**
     * Supported MIME type
     */
    val mimeTypes: Set<String>

    /**
     * Example of file content
     */
    val example: String

    /**
     * Parsing of the document.
     */
    fun parsePackageVersions(defaultType: PackageType, doc: Document): List<PackageVersion>

}