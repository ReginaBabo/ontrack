package net.nemerosa.ontrack.model.structure

import com.fasterxml.jackson.annotation.JsonProperty

data class BuildLinkForm(
        @JsonProperty("addOnly")
        val isAddOnly: Boolean,
        val links: List<BuildLinkFormItem>
)
