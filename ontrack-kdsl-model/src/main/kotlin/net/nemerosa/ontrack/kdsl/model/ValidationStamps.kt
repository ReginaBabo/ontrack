package net.nemerosa.ontrack.kdsl.model

/**
 * Validation stamp entity
 *
 * @property name Name of this validation stamp
 * @property description Description of this validation stamp
 */
class ValidationStamp(
        id: Int,
        creation: Signature,
        val name: String,
        val description: String
) : ProjectEntityResource(id, creation)

/**
 * Creates a validation stamp.
 *
 * @param name Name of the validation stamp
 * @param description Description of the validation stamp
 * @return Created validation stamp
 */
fun Branch.createValidationStamp(
        name: String,
        description: String = ""
): ValidationStamp =
        ontrackConnector.post(
                "structure/branches/$id/validationStamps/create",
                mapOf(
                        "name" to name,
                        "description" to description
                )
        ).toConnector()

/**
 * Creates a validation stamp and runs some code for it.
 *
 * @param T Type of object returned by this function
 * @param name Name of the validation stamp
 * @param description Description of the validation stamp
 * @param initFn Code to run against the created validation stamp
 * @return Object return by [initFn]
 */
fun <T> Branch.validationStamp(
        name: String,
        description: String = "",
        initFn: ValidationStamp.() -> T
): T = createValidationStamp(name, description).initFn()
