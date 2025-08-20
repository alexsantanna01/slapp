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

    @Named("roomId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RoomDTO toDtoRoomId(Room room);
}
