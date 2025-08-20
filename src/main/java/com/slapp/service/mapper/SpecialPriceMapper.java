package com.slapp.service.mapper;

import com.slapp.domain.Room;
import com.slapp.domain.SpecialPrice;
import com.slapp.service.dto.RoomDTO;
import com.slapp.service.dto.SpecialPriceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SpecialPrice} and its DTO {@link SpecialPriceDTO}.
 */
@Mapper(componentModel = "spring")
public interface SpecialPriceMapper extends EntityMapper<SpecialPriceDTO, SpecialPrice> {
    @Mapping(target = "room", source = "room", qualifiedByName = "roomId")
    SpecialPriceDTO toDto(SpecialPrice s);

    @Named("roomId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RoomDTO toDtoRoomId(Room room);
}
