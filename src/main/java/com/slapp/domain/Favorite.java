package com.slapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Entidade para gerenciar favoritos dos usuários
 */
@Entity
@Table(name = "favorite", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "studio_id" }))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Favorite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "authorities" }, allowSetters = true)
    private User user;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "rooms", "operatingHours" }, allowSetters = true)
    private Studio studio;

    // Constructors
    public Favorite() {}

    public Favorite(User user, Studio studio) {
        this.user = user;
        this.studio = studio;
        this.createdDate = Instant.now();
    }

    // Getters and Setters
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Studio getStudio() {
        return studio;
    }

    public void setStudio(Studio studio) {
        this.studio = studio;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Favorite)) return false;
        return getId() != null && getId().equals(((Favorite) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "Favorite{" +
            "id=" +
            getId() +
            ", createdDate='" +
            getCreatedDate() +
            "'" +
            ", userId=" +
            (getUser() != null ? getUser().getId() : null) +
            ", studioId=" +
            (getStudio() != null ? getStudio().getId() : null) +
            "}"
        );
    }
}
