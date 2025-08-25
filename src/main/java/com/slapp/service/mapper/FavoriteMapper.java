package com.slapp.service.mapper;

import com.slapp.domain.Favorite;
import com.slapp.domain.Studio;
import com.slapp.domain.User;
import com.slapp.service.dto.FavoriteDTO;
import com.slapp.service.dto.StudioDTO;
import com.slapp.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Favorite} and its DTO {@link FavoriteDTO}.
 */
@Mapper(componentModel = "spring")
public interface FavoriteMapper extends EntityMapper<FavoriteDTO, Favorite> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "studio", source = "studio", qualifiedByName = "studioName")
    FavoriteDTO toDto(Favorite s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("studioName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    StudioDTO toDtoStudioName(Studio studio);
}
