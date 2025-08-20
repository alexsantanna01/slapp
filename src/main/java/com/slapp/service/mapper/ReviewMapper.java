package com.slapp.service.mapper;

import com.slapp.domain.Reservation;
import com.slapp.domain.Review;
import com.slapp.domain.Studio;
import com.slapp.domain.UserProfile;
import com.slapp.service.dto.ReservationDTO;
import com.slapp.service.dto.ReviewDTO;
import com.slapp.service.dto.StudioDTO;
import com.slapp.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Review} and its DTO {@link ReviewDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReviewMapper extends EntityMapper<ReviewDTO, Review> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "userProfileId")
    @Mapping(target = "studio", source = "studio", qualifiedByName = "studioId")
    @Mapping(target = "reservation", source = "reservation", qualifiedByName = "reservationId")
    ReviewDTO toDto(Review s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);

    @Named("studioId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StudioDTO toDtoStudioId(Studio studio);

    @Named("reservationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReservationDTO toDtoReservationId(Reservation reservation);
}
