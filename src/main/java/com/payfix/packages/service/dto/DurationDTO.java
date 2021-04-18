package com.payfix.packages.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.payfix.packages.domain.Duration} entity.
 */
public class DurationDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DurationDTO)) {
            return false;
        }

        DurationDTO durationDTO = (DurationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, durationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DurationDTO{" +
            "id=" + getId() +
            ", value=" + getValue() +
            "}";
    }
}
