package com.slapp.service.dto;

import com.slapp.repository.projections.RoomImageProjection;
import com.slapp.repository.projections.StudioDetailProjection;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO que implementa StudioDetailProjection para facilitar a montagem dos dados
 */
public class StudioDetailDTO implements StudioDetailProjection {

    // Dados do Studio
    private Long id;
    private String name;
    private String description;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private Double latitude;
    private Double longitude;
    private String phone;
    private String email;
    private String website;
    private String image;
    private Boolean active;
    private Instant createdAt;
    private Instant updatedAt;

    private OwnerDTO owner;
    private List<RoomDTO> rooms = new ArrayList<>();

    // Implementação dos getters da interface
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public String getZipCode() {
        return zipCode;
    }

    @Override
    public Double getLatitude() {
        return latitude;
    }

    @Override
    public Double getLongitude() {
        return longitude;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getWebsite() {
        return website;
    }

    @Override
    public String getImage() {
        return image;
    }

    @Override
    public Boolean getActive() {
        return active;
    }

    @Override
    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public StudioDetailProjection.OwnerProjection getOwner() {
        return owner;
    }

    @Override
    public List<StudioDetailProjection.RoomDetailProjection> getRooms() {
        return new ArrayList<>(rooms);
    }

    // Setters para montagem do DTO
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setOwner(OwnerDTO owner) {
        this.owner = owner;
    }

    public void setRooms(List<RoomDTO> rooms) {
        this.rooms = rooms;
    }

    /**
     * DTO para Owner
     */
    public static class OwnerDTO implements StudioDetailProjection.OwnerProjection {

        private Long id;

        @Override
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    /**
     * DTO para Room
     */
    public static class RoomDTO implements StudioDetailProjection.RoomDetailProjection {

        private Long id;
        private String name;
        private String description;
        private BigDecimal hourlyRate;
        private Integer capacity;
        private Boolean soundproofed;
        private Boolean airConditioning;
        private String roomType;
        private Boolean active;
        private Instant createdAt;
        private Instant updatedAt;
        private List<RoomImageDTO> roomImages = new ArrayList<>();

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public BigDecimal getHourlyRate() {
            return hourlyRate;
        }

        @Override
        public Integer getCapacity() {
            return capacity;
        }

        @Override
        public Boolean getSoundproofed() {
            return soundproofed;
        }

        @Override
        public Boolean getAirConditioning() {
            return airConditioning;
        }

        @Override
        public String getRoomType() {
            return roomType;
        }

        @Override
        public Boolean getActive() {
            return active;
        }

        @Override
        public Instant getCreatedAt() {
            return createdAt;
        }

        @Override
        public Instant getUpdatedAt() {
            return updatedAt;
        }

        @Override
        public List<RoomImageProjection> getRoomImages() {
            return new ArrayList<>(roomImages);
        }

        // Setters
        public void setId(Long id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setHourlyRate(BigDecimal hourlyRate) {
            this.hourlyRate = hourlyRate;
        }

        public void setCapacity(Integer capacity) {
            this.capacity = capacity;
        }

        public void setSoundproofed(Boolean soundproofed) {
            this.soundproofed = soundproofed;
        }

        public void setAirConditioning(Boolean airConditioning) {
            this.airConditioning = airConditioning;
        }

        public void setRoomType(String roomType) {
            this.roomType = roomType;
        }

        public void setActive(Boolean active) {
            this.active = active;
        }

        public void setCreatedAt(Instant createdAt) {
            this.createdAt = createdAt;
        }

        public void setUpdatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
        }

        public void setRoomImages(List<RoomImageDTO> roomImages) {
            this.roomImages = roomImages;
        }
    }

    /**
     * DTO para RoomImage
     */
    public static class RoomImageDTO implements RoomImageProjection {

        private Long id;
        private String url;
        private String altText;
        private Integer displayOrder;
        private Boolean active;
        private Long roomId;

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public String getUrl() {
            return url;
        }

        @Override
        public String getAltText() {
            return altText;
        }

        @Override
        public Integer getDisplayOrder() {
            return displayOrder;
        }

        @Override
        public Boolean getActive() {
            return active;
        }

        @Override
        public Long getRoomId() {
            return roomId;
        }

        // Setters
        public void setId(Long id) {
            this.id = id;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setAltText(String altText) {
            this.altText = altText;
        }

        public void setDisplayOrder(Integer displayOrder) {
            this.displayOrder = displayOrder;
        }

        public void setActive(Boolean active) {
            this.active = active;
        }

        public void setRoomId(Long roomId) {
            this.roomId = roomId;
        }
    }
}
