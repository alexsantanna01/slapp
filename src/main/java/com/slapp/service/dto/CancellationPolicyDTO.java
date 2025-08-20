package com.slapp.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.slapp.domain.CancellationPolicy} entity.
 */
@Schema(description = "Políticas de cancelamento")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CancellationPolicyDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String name;

    @Lob
    private String description;

    @NotNull
    @Min(value = 0)
    private Integer hoursBeforeEvent;

    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    private Integer refundPercentage;

    @NotNull
    private Boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getHoursBeforeEvent() {
        return hoursBeforeEvent;
    }

    public void setHoursBeforeEvent(Integer hoursBeforeEvent) {
        this.hoursBeforeEvent = hoursBeforeEvent;
    }

    public Integer getRefundPercentage() {
        return refundPercentage;
    }

    public void setRefundPercentage(Integer refundPercentage) {
        this.refundPercentage = refundPercentage;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CancellationPolicyDTO)) {
            return false;
        }

        CancellationPolicyDTO cancellationPolicyDTO = (CancellationPolicyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cancellationPolicyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CancellationPolicyDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", hoursBeforeEvent=" + getHoursBeforeEvent() +
            ", refundPercentage=" + getRefundPercentage() +
            ", active='" + getActive() + "'" +
            "}";
    }
}
