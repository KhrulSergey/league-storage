package com.freetonleague.storage.domain.enums;


public enum ResourceFileType {
    IMAGE("images"),
    DOCUMENT("documents"),
    VIDEO("videos"),
    ;

    private final String folderName;

    ResourceFileType(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return folderName;
    }

    public boolean isImage() {
        return this == IMAGE;
    }
}
