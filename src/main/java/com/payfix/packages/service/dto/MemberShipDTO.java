package com.payfix.packages.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.payfix.packages.domain.MemberShip} entity.
 */
public class MemberShipDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    private String description;

    private Double price;

    private Long durationId;

    private Long durationUnitId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getDurationId() {
        return durationId;
    }

    public void setDurationId(Long durationId) {
        this.durationId = durationId;
    }

    public Long getDurationUnitId() {
        return durationUnitId;
    }

    public void setDurationUnitId(Long durationUnitId) {
        this.durationUnitId = durationUnitId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MemberShipDTO)) {
            return false;
        }

        MemberShipDTO memberShipDTO = (MemberShipDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, memberShipDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MemberShipDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", price=" + getPrice() +
            ", durationId=" + getDurationId() +
            ", durationUnitId=" + getDurationUnitId() +
            "}";
    }
}
