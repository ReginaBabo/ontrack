package net.nemerosa.ontrack.model

import com.fasterxml.jackson.annotation.JsonProperty

data class JavaPOJO(
        val name: String,
        val value: Int
) {
    val doubleValue: Int
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        get() = value * 2

}
