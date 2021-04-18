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
 * Criteria class for the {@link com.payfix.packages.domain.DurationUnit} entity. This class is used
 * in {@link com.payfix.packages.web.rest.DurationUnitResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /duration-units?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DurationUnitCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter unitValue;

    private StringFilter alies;

    private StringFilter name;

    public DurationUnitCriteria() {}

    public DurationUnitCriteria(DurationUnitCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.unitValue = other.unitValue == null ? null : other.unitValue.copy();
        this.alies = other.alies == null ? null : other.alies.copy();
        this.name = other.name == null ? null : other.name.copy();
    }

    @Override
    public DurationUnitCriteria copy() {
        return new DurationUnitCriteria(this);
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

    public IntegerFilter getUnitValue() {
        return unitValue;
    }

    public IntegerFilter unitValue() {
        if (unitValue == null) {
            unitValue = new IntegerFilter();
        }
        return unitValue;
    }

    public void setUnitValue(IntegerFilter unitValue) {
        this.unitValue = unitValue;
    }

    public StringFilter getAlies() {
        return alies;
    }

    public StringFilter alies() {
        if (alies == null) {
            alies = new StringFilter();
        }
        return alies;
    }

    public void setAlies(StringFilter alies) {
        this.alies = alies;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DurationUnitCriteria that = (DurationUnitCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(unitValue, that.unitValue) &&
            Objects.equals(alies, that.alies) &&
            Objects.equals(name, that.name)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, unitValue, alies, name);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DurationUnitCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (unitValue != null ? "unitValue=" + unitValue + ", " : "") +
            (alies != null ? "alies=" + alies + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            "}";
    }
}
