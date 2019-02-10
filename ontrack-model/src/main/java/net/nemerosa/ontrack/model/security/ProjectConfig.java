package net.nemerosa.ontrack.model.security;

import net.nemerosa.ontrack.model.labels.ProjectLabelManagement;
import net.nemerosa.ontrack.model.labels.ProjectPackageManagement;

/**
 * Configuration for a project.
 */
@CoreFunction
public interface ProjectConfig extends ProjectView, ProjectLabelManagement, ProjectPackageManagement {
}
