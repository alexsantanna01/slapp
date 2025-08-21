package com.slapp.repository.projections;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Projection para Room
 */
public interface RoomProjection {
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
}
