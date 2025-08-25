package com.slapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.slapp.domain.Favorite} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FavoriteDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdDate;

    private UserDTO user;

    private StudioDTO studio;

    public FavoriteDTO() {}

    public FavoriteDTO(Long id, Instant createdDate, UserDTO user, StudioDTO studio) {
        this.id = id;
        this.createdDate = createdDate;
        this.user = user;
        this.studio = studio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public StudioDTO getStudio() {
        return studio;
    }

    public void setStudio(StudioDTO studio) {
        this.studio = studio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FavoriteDTO)) {
            return false;
        }

        FavoriteDTO favoriteDTO = (FavoriteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, favoriteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return (
            "FavoriteDTO{" +
            "id=" +
            getId() +
            ", createdDate='" +
            getCreatedDate() +
            "'" +
            ", user=" +
            getUser() +
            ", studio=" +
            getStudio() +
            "}"
        );
    }
}
