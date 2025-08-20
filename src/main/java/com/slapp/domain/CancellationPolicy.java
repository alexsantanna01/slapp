package com.slapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Políticas de cancelamento
 */
@Entity
@Table(name = "cancellation_policy")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CancellationPolicy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @NotNull
    @Min(value = 0)
    @Column(name = "hours_before_event", nullable = false)
    private Integer hoursBeforeEvent;

    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "refund_percentage", nullable = false)
    private Integer refundPercentage;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CancellationPolicy id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public CancellationPolicy name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public CancellationPolicy description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getHoursBeforeEvent() {
        return this.hoursBeforeEvent;
    }

    public CancellationPolicy hoursBeforeEvent(Integer hoursBeforeEvent) {
        this.setHoursBeforeEvent(hoursBeforeEvent);
        return this;
    }

    public void setHoursBeforeEvent(Integer hoursBeforeEvent) {
        this.hoursBeforeEvent = hoursBeforeEvent;
    }

    public Integer getRefundPercentage() {
        return this.refundPercentage;
    }

    public CancellationPolicy refundPercentage(Integer refundPercentage) {
        this.setRefundPercentage(refundPercentage);
        return this;
    }

    public void setRefundPercentage(Integer refundPercentage) {
        this.refundPercentage = refundPercentage;
    }

    public Boolean getActive() {
        return this.active;
    }

    public CancellationPolicy active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CancellationPolicy)) {
            return false;
        }
        return getId() != null && getId().equals(((CancellationPolicy) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CancellationPolicy{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", hoursBeforeEvent=" + getHoursBeforeEvent() +
            ", refundPercentage=" + getRefundPercentage() +
            ", active='" + getActive() + "'" +
            "}";
    }
}
