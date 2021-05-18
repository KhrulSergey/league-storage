package com.freetonleague.storage.domain.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.freetonleague.storage.domain.enums.ResourceFileType;
import com.freetonleague.storage.domain.enums.ResourcePrivacyType;
import com.freetonleague.storage.domain.enums.ResourceStatusType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

@SuperBuilder
@Data
@NoArgsConstructor
public class MediaResourceDto {

    /**
     * Not null for adding resource
     */
    private String rawData;

    private MultipartFile multipartFile;

    @Size(max = 200)
    private String name;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String extension;

    @NotNull
    private ResourceFileType resourceType;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String hashKey;

    @Builder.Default
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ResourceStatusType status = ResourceStatusType.ACTIVE;

    @NotNull
    private ResourcePrivacyType privacyType;

    /**
     * Owner reference (League-id for user, CoreId for team)
     * Used only for files that can be accessed by one owner.
     */
    private UUID privacyOwnerGUID;

    @NotNull
    private UUID creatorGUID;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedAt;
}
