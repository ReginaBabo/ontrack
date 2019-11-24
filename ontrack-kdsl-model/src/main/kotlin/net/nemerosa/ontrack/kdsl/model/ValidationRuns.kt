package net.nemerosa.ontrack.kdsl.model

/**
 * Validates a build.
 *
 * @receiver Build to validate
 * @param validationStamp Stamp to apply
 * @param status Validation status
 * TODO @return Validation run
 */
fun Build.validate(
        validationStamp: String,
        status: String? = null,
        description: String = ""
) {
    ontrackConnector.post(
            "structure/builds/$id/validationRuns/create",
            mapOf(
                    "validationStampName" to validationStamp,
                    "validationRunStatusId" to status,
                    "description" to description
            )
    )
}