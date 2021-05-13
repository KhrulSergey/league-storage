package com.freetonleague.storage.domain.enums;


public enum ResourcePrivacyType {
    OPEN,
    RESTRICTED,
    PRIVATE,
    ;

    public boolean isOpen(){
        return this == OPEN;
    }

    public boolean isPrivate(){
        return this == PRIVATE;
    }
}
