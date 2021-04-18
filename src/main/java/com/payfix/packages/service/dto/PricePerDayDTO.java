package com.payfix.packages.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.payfix.packages.domain.PricePerDay} entity.
 */
public class PricePerDayDTO implements Serializable {

    private Long id;

    @NotNull
    private Double price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PricePerDayDTO)) {
            return false;
        }

        PricePerDayDTO pricePerDayDTO = (PricePerDayDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pricePerDayDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PricePerDayDTO{" +
            "id=" + getId() +
            ", price=" + getPrice() +
            "}";
    }
}
