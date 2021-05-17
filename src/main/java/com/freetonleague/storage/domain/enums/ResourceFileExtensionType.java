package com.freetonleague.storage.domain.enums;

public enum ResourceFileExtensionType {

    JPEG("jpeg", ResourceFileType.IMAGE),
    JPG("jpg", ResourceFileType.IMAGE),
    PNG("png", ResourceFileType.IMAGE),
    BMP("bmp", ResourceFileType.IMAGE),
    ;

    private final String value;
    private final ResourceFileType fileType;

    ResourceFileExtensionType(String value, ResourceFileType fileType) {
        this.value = value;
        this.fileType = fileType;
    }

    public static ResourceFileExtensionType fromString(String text) {
        for (ResourceFileExtensionType b : ResourceFileExtensionType.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public ResourceFileType getFileType() {
        return fileType;
    }
}
