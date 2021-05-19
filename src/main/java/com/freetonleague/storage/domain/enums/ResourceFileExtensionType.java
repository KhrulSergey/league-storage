package com.freetonleague.storage.domain.enums;

public enum ResourceFileExtensionType {

    JPEG("jpeg", "image/jpeg", ResourceFileType.IMAGE),
    JPG("jpg", "image/jpg", ResourceFileType.IMAGE),
    PNG("png", "image/png", ResourceFileType.IMAGE),
    BMP("bmp", "image/bmp", ResourceFileType.IMAGE),
    ;

    private final String extension;
    private final String httpExtension;
    private final ResourceFileType fileType;

    ResourceFileExtensionType(String extension, String httpExtension, ResourceFileType fileType) {
        this.extension = extension;
        this.httpExtension = httpExtension;
        this.fileType = fileType;
    }

    public static ResourceFileExtensionType fromExtension(String extension) {
        for (ResourceFileExtensionType b : ResourceFileExtensionType.values()) {
            if (b.extension.equalsIgnoreCase(extension)) {
                return b;
            }
        }
        return null;
    }

    public static ResourceFileExtensionType fromHttpExtension(String httpExtension) {
        for (ResourceFileExtensionType b : ResourceFileExtensionType.values()) {
            if (httpExtension.contains(b.httpExtension)) {
                return b;
            }
        }
        return null;
    }

    public String getExtension() {
        return extension;
    }

    public ResourceFileType getFileType() {
        return fileType;
    }

    public String getHttpExtension() {
        return httpExtension;
    }
}
