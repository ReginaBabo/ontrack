package net.nemerosa.ontrack.extension.issues.support;

import net.nemerosa.ontrack.extension.issues.model.IssueStatus;

public enum MockIssueStatus implements IssueStatus {

    OPEN,

    CLOSED;

    @Override
    public String getName() {
        return name();
    }

}
