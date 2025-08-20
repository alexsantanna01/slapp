package com.slapp.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.slapp.domain.RoomImage} entity.
 */
@Schema(description = "Imagens das salas")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RoomImageDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 500)
    private String url;

    @Size(max = 100)
    private String altText;

    private Integer displayOrder;

    @NotNull
    private Boolean active;

    @NotNull
    private RoomDTO room;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public RoomDTO getRoom() {
        return room;
    }

    public void setRoom(RoomDTO room) {
        this.room = room;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoomImageDTO)) {
            return false;
        }

        RoomImageDTO roomImageDTO = (RoomImageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, roomImageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoomImageDTO{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", altText='" + getAltText() + "'" +
            ", displayOrder=" + getDisplayOrder() +
            ", active='" + getActive() + "'" +
            ", room=" + getRoom() +
            "}";
    }
}
