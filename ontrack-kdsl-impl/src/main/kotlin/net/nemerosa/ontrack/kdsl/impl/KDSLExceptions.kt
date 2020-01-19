package net.nemerosa.ontrack.kdsl.impl

import net.nemerosa.ontrack.kdsl.model.DSLException

class ResourceMissingLinkException(name: String) : DSLException("Link is missing: $name")
