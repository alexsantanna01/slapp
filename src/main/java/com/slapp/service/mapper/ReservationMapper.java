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
@Mapper(componentModel = "spring", uses = { UserProfileMapper.class, RoomMapper.class })
public interface ReservationMapper extends EntityMapper<ReservationDTO, Reservation> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "userProfileId")
    @Mapping(target = "room", source = "room", qualifiedByName = "roomId")
    ReservationDTO toDto(Reservation s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "user.id", source = "user.id")
    @Mapping(target = "user.login", source = "user.login")
    @Mapping(target = "user.firstName", source = "user.firstName")
    @Mapping(target = "user.lastName", source = "user.lastName")
    @Mapping(target = "user.email", source = "user.email")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);

    @Named("roomId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "studio.id", source = "studio.id")
    @Mapping(target = "studio.name", source = "studio.name")
    RoomDTO toDtoRoomId(Room room);
}
