package com.slapp.service.mapper;

import com.slapp.domain.Availability;
import com.slapp.domain.Room;
import com.slapp.service.dto.AvailabilityDTO;
import com.slapp.service.dto.RoomDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Availability} and its DTO {@link AvailabilityDTO}.
 */
@Mapper(componentModel = "spring")
public interface AvailabilityMapper extends EntityMapper<AvailabilityDTO, Availability> {
    @Mapping(target = "room", source = "room", qualifiedByName = "roomId")
    AvailabilityDTO toDto(Availability s);

    @Named("roomId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RoomDTO toDtoRoomId(Room room);
}
