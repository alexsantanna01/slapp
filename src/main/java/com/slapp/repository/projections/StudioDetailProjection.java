package com.slapp.repository.projections;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Projection composta para detalhes completos do Studio
 */
public interface StudioDetailProjection {
    Long getId();
    String getName();
    String getDescription();
    String getAddress();
    String getCity();
    String getState();
    String getZipCode();
    Double getLatitude();
    Double getLongitude();
    String getPhone();
    String getEmail();
    String getWebsite();
    String getImage();
    Boolean getActive();
    Instant getCreatedAt();
    Instant getUpdatedAt();

    // Interface aninhada para Owner
    interface OwnerProjection {
        Long getId();
    }

    OwnerProjection getOwner();
    java.util.List<RoomDetailProjection> getRooms();

    // Interface aninhada para Room com imagens
    interface RoomDetailProjection {
        Long getId();
        String getName();
        String getDescription();
        BigDecimal getHourlyRate();
        Integer getCapacity();
        Boolean getSoundproofed();
        Boolean getAirConditioning();
        String getRoomType();
        Boolean getActive();
        Instant getCreatedAt();
        Instant getUpdatedAt();
        java.util.List<RoomImageProjection> getRoomImages();
    }
}
