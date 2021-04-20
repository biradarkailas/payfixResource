package com.payfix.packages.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.payfix.packages.domain.Logo} entity. This class is used
 * in {@link com.payfix.packages.web.rest.LogoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /logos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LogoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter imageUrl;

    private StringFilter termConditions;

    private StringFilter about;

    private LongFilter userId;

    private LongFilter categoryId;

    private LongFilter durationId;

    private LongFilter durationUnitId;

    private LongFilter pricePerDayId;

    public LogoCriteria() {}

    public LogoCriteria(LogoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.imageUrl = other.imageUrl == null ? null : other.imageUrl.copy();
        this.termConditions = other.termConditions == null ? null : other.termConditions.copy();
        this.about = other.about == null ? null : other.about.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
        this.durationId = other.durationId == null ? null : other.durationId.copy();
        this.durationUnitId = other.durationUnitId == null ? null : other.durationUnitId.copy();
        this.pricePerDayId = other.pricePerDayId == null ? null : other.pricePerDayId.copy();
    }

    @Override
    public LogoCriteria copy() {
        return new LogoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getImageUrl() {
        return imageUrl;
    }

    public StringFilter imageUrl() {
        if (imageUrl == null) {
            imageUrl = new StringFilter();
        }
        return imageUrl;
    }

    public void setImageUrl(StringFilter imageUrl) {
        this.imageUrl = imageUrl;
    }

    public StringFilter getTermConditions() {
        return termConditions;
    }

    public StringFilter termConditions() {
        if (termConditions == null) {
            termConditions = new StringFilter();
        }
        return termConditions;
    }

    public void setTermConditions(StringFilter termConditions) {
        this.termConditions = termConditions;
    }

    public StringFilter getAbout() {
        return about;
    }

    public StringFilter about() {
        if (about == null) {
            about = new StringFilter();
        }
        return about;
    }

    public void setAbout(StringFilter about) {
        this.about = about;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public LongFilter categoryId() {
        if (categoryId == null) {
            categoryId = new LongFilter();
        }
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }

    public LongFilter getDurationId() {
        return durationId;
    }

    public LongFilter durationId() {
        if (durationId == null) {
            durationId = new LongFilter();
        }
        return durationId;
    }

    public void setDurationId(LongFilter durationId) {
        this.durationId = durationId;
    }

    public LongFilter getDurationUnitId() {
        return durationUnitId;
    }

    public LongFilter durationUnitId() {
        if (durationUnitId == null) {
            durationUnitId = new LongFilter();
        }
        return durationUnitId;
    }

    public void setDurationUnitId(LongFilter durationUnitId) {
        this.durationUnitId = durationUnitId;
    }

    public LongFilter getPricePerDayId() {
        return pricePerDayId;
    }

    public LongFilter pricePerDayId() {
        if (pricePerDayId == null) {
            pricePerDayId = new LongFilter();
        }
        return pricePerDayId;
    }

    public void setPricePerDayId(LongFilter pricePerDayId) {
        this.pricePerDayId = pricePerDayId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LogoCriteria that = (LogoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(imageUrl, that.imageUrl) &&
            Objects.equals(termConditions, that.termConditions) &&
            Objects.equals(about, that.about) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(durationId, that.durationId) &&
            Objects.equals(durationUnitId, that.durationUnitId) &&
            Objects.equals(pricePerDayId, that.pricePerDayId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, imageUrl, termConditions, about, userId, categoryId, durationId, durationUnitId, pricePerDayId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LogoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (imageUrl != null ? "imageUrl=" + imageUrl + ", " : "") +
            (termConditions != null ? "termConditions=" + termConditions + ", " : "") +
            (about != null ? "about=" + about + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
            (durationId != null ? "durationId=" + durationId + ", " : "") +
            (durationUnitId != null ? "durationUnitId=" + durationUnitId + ", " : "") +
            (pricePerDayId != null ? "pricePerDayId=" + pricePerDayId + ", " : "") +
            "}";
    }
}
