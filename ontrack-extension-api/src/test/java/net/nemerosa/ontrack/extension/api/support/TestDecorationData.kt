package net.nemerosa.ontrack.extension.api.support

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Decoration test data for [TestDecorator].
 */
class TestDecorationData(
        val value: String?,
        @JsonProperty("valid")
        val isValid: Boolean
)
