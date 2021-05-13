package com.freetonleague.storage.domain.enums;

public enum ResourceStatusType {
    ACTIVE,
    DELETED
    ;

    public boolean isActive() {
        return this == ACTIVE;
    }
    public boolean isDeleted() {
        return this == DELETED;
    }

}
