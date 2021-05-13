package com.freetonleague.storage.domain.enums;


public enum ResourceFileType {
    IMAGE,
    DOCUMENT,
    VIDEO,
    ;

    public boolean isImage(){
        return this == IMAGE;
    }
}
