package com.slapp.repository.projections;

/**
 * Projection para RoomImage
 */
public interface RoomImageProjection {
    Long getId();
    String getUrl();
    String getAltText();
    Integer getDisplayOrder();
    Boolean getActive();
    Long getRoomId();
}
