package net.nemerosa.ontrack.model.labels

import net.nemerosa.ontrack.model.security.CoreFunction
import net.nemerosa.ontrack.model.security.ProjectFunction

/**
 * Authorization to manage the package identifiers of a project
 */
@CoreFunction
interface ProjectPackageManagement : ProjectFunction
