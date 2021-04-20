package com.payfix.packages.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.payfix.packages.domain.MemberShip} entity.
 */
public class MemberShipDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    private String description;

    private Double price;

    private DurationDTO duration;

    private DurationUnitDTO durationUnit;

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

    public DurationDTO getDuration() {
        return duration;
    }

    public void setDuration(DurationDTO duration) {
        this.duration = duration;
    }

    public DurationUnitDTO getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(DurationUnitDTO durationUnit) {
        this.durationUnit = durationUnit;
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
            ", duration=" + getDuration() +
            ", durationUnit=" + getDurationUnit() +
            "}";
    }
}
