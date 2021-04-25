package com.payfix.packages.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.payfix.packages.domain.Logo} entity.
 */
public class LogoDTO implements Serializable {

    private Long id;

    @NotNull
    private String imageUrl;

    private String termConditions;

    private String about;

    private Long userId;

    private Long categoryId;

    private Long durationId;

    private Long durationUnitId;

    private Long pricePerDayId;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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

    public Long getPricePerDayId() {
        return pricePerDayId;
    }

    public void setPricePerDayId(Long pricePerDayId) {
        this.pricePerDayId = pricePerDayId;
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
            ", user=" + getUserId() +
            ", category=" + getCategoryId() +
            ", duration=" + getDurationId() +
            ", durationUnit=" + getDurationUnitId() +
            ", pricePerDay=" + getPricePerDayId() +
            "}";
    }
}
