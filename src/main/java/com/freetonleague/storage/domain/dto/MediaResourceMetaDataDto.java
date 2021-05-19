package com.freetonleague.storage.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class MediaResourceMetaDataDto implements Serializable {

    private Integer size;

    private String dimension;

    private String format;

    @Min(0)
    @Max(1280) //1280px
    private Integer width;

    @Min(0)
    @Max(1280) //1280px
    private Integer height;

    private Integer horizontalResolution;

    private Integer verticalResolution;

    private Integer bitDepth;

    private Integer totalVideoBitRate;

    private Integer frameVideoRate;

    private Integer totalAudioRate;

    private Integer createdDate;
}
