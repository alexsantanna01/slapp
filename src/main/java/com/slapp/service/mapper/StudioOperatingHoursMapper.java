package com.slapp.service.mapper;

import com.slapp.domain.Studio;
import com.slapp.domain.StudioOperatingHours;
import com.slapp.service.dto.StudioOperatingHoursDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StudioOperatingHours} and its DTO {@link StudioOperatingHoursDTO}.
 */
@Mapper(componentModel = "spring")
public interface StudioOperatingHoursMapper extends EntityMapper<StudioOperatingHoursDTO, StudioOperatingHours> {
    @Mapping(target = "studioId", source = "studio.id")
    StudioOperatingHoursDTO toDto(StudioOperatingHours studioOperatingHours);

    @Mapping(target = "studio", ignore = true)
    StudioOperatingHours toEntity(StudioOperatingHoursDTO studioOperatingHoursDTO);

    default Studio map(Long studioId) {
        if (studioId == null) {
            return null;
        }
        Studio studio = new Studio();
        studio.setId(studioId);
        return studio;
    }
}
