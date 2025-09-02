package com.slapp.service.mapper;

import com.slapp.domain.Room;
import com.slapp.domain.RoomImage;
import com.slapp.service.dto.RoomDTO;
import com.slapp.service.dto.RoomImageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RoomImage} and its DTO {@link RoomImageDTO}.
 */
@Mapper(componentModel = "spring")
public interface RoomImageMapper extends EntityMapper<RoomImageDTO, RoomImage> {
    @Mapping(target = "room", source = "room", qualifiedByName = "roomId")
    RoomImageDTO toDto(RoomImage s);

    @Mapping(target = "room", source = "room", qualifiedByName = "roomFromId")
    RoomImage toEntity(RoomImageDTO roomImageDTO);

    @Named("roomId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RoomDTO toDtoRoomId(Room room);

    @Named("roomFromId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Room toEntityRoomFromId(RoomDTO roomDTO);
}
