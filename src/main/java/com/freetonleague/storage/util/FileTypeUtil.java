package com.freetonleague.storage.util;

import com.freetonleague.storage.domain.enums.ResourceFileExtensionType;

import static java.util.Objects.isNull;

public class FileTypeUtil {
    public static String getFolderNameByFileExtension(String extension) {
        ResourceFileExtensionType fileExtension = ResourceFileExtensionType.fromString(extension);
        if (isNull(fileExtension)) {
            return null;
        }
        return fileExtension.getFileType().getFolderName();
    }
}
