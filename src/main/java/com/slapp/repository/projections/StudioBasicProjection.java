package com.slapp.repository.projections;

import java.time.Instant;

public interface StudioBasicProjection {
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
    Long getOwnerId();
}
