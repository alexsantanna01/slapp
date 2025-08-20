package com.slapp.service.mapper;

import com.slapp.domain.CancellationPolicy;
import com.slapp.domain.Studio;
import com.slapp.domain.UserProfile;
import com.slapp.service.dto.CancellationPolicyDTO;
import com.slapp.service.dto.StudioDTO;
import com.slapp.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Studio} and its DTO {@link StudioDTO}.
 */
@Mapper(componentModel = "spring")
public interface StudioMapper extends EntityMapper<StudioDTO, Studio> {
    @Mapping(target = "owner", source = "owner", qualifiedByName = "userProfileId")
    @Mapping(target = "cancellationPolicy", source = "cancellationPolicy", qualifiedByName = "cancellationPolicyId")
    StudioDTO toDto(Studio s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);

    @Named("cancellationPolicyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CancellationPolicyDTO toDtoCancellationPolicyId(CancellationPolicy cancellationPolicy);
}
