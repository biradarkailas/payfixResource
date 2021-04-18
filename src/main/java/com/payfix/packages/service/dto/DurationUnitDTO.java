package com.payfix.packages.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.payfix.packages.domain.DurationUnit} entity.
 */
public class DurationUnitDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer unitValue;

    @NotNull
    private String alies;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUnitValue() {
        return unitValue;
    }

    public void setUnitValue(Integer unitValue) {
        this.unitValue = unitValue;
    }

    public String getAlies() {
        return alies;
    }

    public void setAlies(String alies) {
        this.alies = alies;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DurationUnitDTO)) {
            return false;
        }

        DurationUnitDTO durationUnitDTO = (DurationUnitDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, durationUnitDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DurationUnitDTO{" +
            "id=" + getId() +
            ", unitValue=" + getUnitValue() +
            ", alies='" + getAlies() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
