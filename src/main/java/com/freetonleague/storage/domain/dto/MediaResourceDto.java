package com.freetonleague.storage.domain.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.freetonleague.storage.domain.enums.ResourceFileExtensionType;
import com.freetonleague.storage.domain.enums.ResourcePrivacyType;
import com.freetonleague.storage.domain.enums.ResourceStatusType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
    @ApiModelProperty(required = true)
    @NotNull
    private String rawData;

    @ApiModelProperty(readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String hashKey;

    @ApiModelProperty(required = true)
    @Size(max = 200)
    private String name;

    @ApiModelProperty(readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ResourceFileExtensionType resourceType;

    @ApiModelProperty(readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Min(0)
    @Max(5242880) //5Mb
    private Integer sizeInBytes;

    @ApiModelProperty(readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private MediaResourceMetaDataDto resourceMetaData;

    @Builder.Default
    @ApiModelProperty(readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ResourceStatusType status = ResourceStatusType.ACTIVE;

    @ApiModelProperty(required = true)
    @NotNull
    private ResourcePrivacyType privacyType;

    /**
     * Owner reference (League-id for user, CoreId for team)
     * Used only for files that can be accessed by one owner.
     */
    private UUID privacyOwnerGUID;

    @ApiModelProperty(required = true)
    @NotNull
    private UUID creatorGUID;

    @ApiModelProperty(readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @ApiModelProperty(readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedAt;

    public String getExtension() {
        return resourceType.getExtension();
    }
}
