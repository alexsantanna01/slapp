package com.slapp.repository.projections;

import java.math.BigDecimal;

public interface StudioListProjection {
    Long getId();
    String getName();
    String getDescription();
    String getAddress();
    String getCity();
    String getState();
    String getImage();
    Long getRoomCount();
    BigDecimal getMinPrice();
    BigDecimal getMaxPrice();
}
