package net.nemerosa.ontrack.kdsl.model

import net.nemerosa.ontrack.dsl.DSLException

class ResourceMissingLinkException(name: String) : DSLException("Link is missing: $name")
