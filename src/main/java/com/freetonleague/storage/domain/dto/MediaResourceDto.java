package com.freetonleague.storage.domain.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.freetonleague.storage.domain.enums.ResourceFileType;
import com.freetonleague.storage.domain.enums.ResourcePrivacyType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@SuperBuilder
@Data
@NoArgsConstructor
public class MediaResourceDto{

    @NotNull
    private ResourceFileType resourceType;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String hashKey;

    @NotNull
    private ResourcePrivacyType privacyType;

    private UUID privacyOwner;

    @NotNull
    private UUID createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedAt;
}
