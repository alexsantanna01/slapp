package com.slapp.service.mapper;

import com.slapp.domain.Reservation;
import com.slapp.domain.Room;
import com.slapp.domain.UserProfile;
import com.slapp.service.dto.ReservationDTO;
import com.slapp.service.dto.RoomDTO;
import com.slapp.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reservation} and its DTO {@link ReservationDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReservationMapper extends EntityMapper<ReservationDTO, Reservation> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "userProfileId")
    @Mapping(target = "room", source = "room", qualifiedByName = "roomId")
    ReservationDTO toDto(Reservation s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);

    @Named("roomId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RoomDTO toDtoRoomId(Room room);
}
