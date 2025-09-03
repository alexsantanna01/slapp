package com.slapp.service.mapper;

import com.slapp.domain.Room;
import com.slapp.domain.Studio;
import com.slapp.service.dto.RoomDTO;
import com.slapp.service.dto.StudioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Room} and its DTO {@link RoomDTO}.
 */
@Mapper(componentModel = "spring")
public interface RoomMapper extends EntityMapper<RoomDTO, Room> {
    @Mapping(target = "studio", source = "studio", qualifiedByName = "studioId")
    RoomDTO toDto(Room s);

    @Mapping(target = "studio", source = "studio", qualifiedByName = "studioFromId")
    Room toEntity(RoomDTO roomDTO);

    @Named("studioId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StudioDTO toDtoStudioId(Studio studio);

    @Named("studioFromId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Studio toEntityStudioFromId(StudioDTO studioDTO);
}
