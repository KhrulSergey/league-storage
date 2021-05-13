package com.freetonleague.storage.mapper;

import com.freetonleague.storage.domain.dto.MediaResourceDto;
import com.freetonleague.storage.domain.model.MediaResource;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MediaResourceMapper {

    MediaResource fromDto(MediaResourceDto dto);

    MediaResourceDto toDto(MediaResource entity);
}
