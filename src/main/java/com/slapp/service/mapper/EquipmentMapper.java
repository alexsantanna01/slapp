package com.slapp.service.mapper;

import com.slapp.domain.Equipment;
import com.slapp.domain.Room;
import com.slapp.service.dto.EquipmentDTO;
import com.slapp.service.dto.RoomDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Equipment} and its DTO {@link EquipmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface EquipmentMapper extends EntityMapper<EquipmentDTO, Equipment> {
    @Mapping(target = "room", source = "room", qualifiedByName = "roomId")
    EquipmentDTO toDto(Equipment s);

    @Named("roomId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RoomDTO toDtoRoomId(Room room);
}
