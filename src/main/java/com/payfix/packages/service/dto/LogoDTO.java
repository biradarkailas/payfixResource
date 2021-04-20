package com.payfix.packages.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.payfix.packages.domain.Logo} entity.
 */
public class LogoDTO implements Serializable {

    private Long id;

    @NotNull
    private String imageUrl;

    private String termConditions;

    private String about;

    private UserDTO user;

    private CategoryDTO category;

    private DurationDTO duration;

    private DurationUnitDTO durationUnit;

    private PricePerDayDTO pricePerDay;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTermConditions() {
        return termConditions;
    }

    public void setTermConditions(String termConditions) {
        this.termConditions = termConditions;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
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

    public PricePerDayDTO getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(PricePerDayDTO pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LogoDTO)) {
            return false;
        }

        LogoDTO logoDTO = (LogoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, logoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LogoDTO{" +
            "id=" + getId() +
            ", imageUrl='" + getImageUrl() + "'" +
            ", termConditions='" + getTermConditions() + "'" +
            ", about='" + getAbout() + "'" +
            ", user=" + getUser() +
            ", category=" + getCategory() +
            ", duration=" + getDuration() +
            ", durationUnit=" + getDurationUnit() +
            ", pricePerDay=" + getPricePerDay() +
            "}";
    }
}
