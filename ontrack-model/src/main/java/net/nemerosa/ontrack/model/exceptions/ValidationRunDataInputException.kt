package net.nemerosa.ontrack.model.exceptions

import net.nemerosa.ontrack.json.JsonParseException

open class ValidationRunDataInputException(pattern: String) : InputException(pattern)

class ValidationRunDataStatusRequiredBecauseNoDataTypeException : ValidationRunDataInputException(
        "Validation Run Status is required because the validation stamp has no data type."
)

class ValidationRunDataStatusRequiredBecauseNoDataException : ValidationRunDataInputException(
        "Validation Run Status is required because no data is provided."
)

class ValidationRunDataJSONInputException(ex: JsonParseException) : ValidationRunDataInputException(
        "Could not parse the JSON for the validation data: ${ex.message}"
)

class ValidationRunDataFormatException(message: String) : ValidationRunDataInputException(message)

class ValidationRunDataMismatchException(
        actualId: String,
        expectedId: String
) : ValidationRunDataInputException(
        "Data associated with the validation run as different " +
                "type than the one associated with the validation stamp. " +
                "`$expectedId` is expected and `$actualId` was given."

)

class ValidationRunDataTypeNotFoundException(type: String) : ValidationRunDataInputException(
        "Cannot find any data type for ID `$type`"
)
