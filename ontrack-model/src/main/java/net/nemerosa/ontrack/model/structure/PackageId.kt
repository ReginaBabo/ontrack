package net.nemerosa.ontrack.model.structure

import com.fasterxml.jackson.annotation.JsonIgnore
import net.nemerosa.ontrack.model.extension.ExtensionFeatureDescription

class PackageId(
        @JsonIgnore
        val type: PackageType,
        val id: String
) {


    /**
     * Gets the decoration type for the decorator name.
     */
    val typeId: String get() = type.javaClass.getName()

    /**
     * Extension feature description
     */
    val feature: ExtensionFeatureDescription get() = type.feature.featureDescription

}