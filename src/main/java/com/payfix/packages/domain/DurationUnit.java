package com.payfix.packages.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DurationUnit.
 */
@Entity
@Table(name = "duration_unit")
public class DurationUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull
    @Column(name = "unit_value", nullable = false)
    private Integer unitValue;

    @NotNull
    @Column(name = "alies", nullable = false, unique = true)
    private String alies;

    @Column(name = "name")
    private String name;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DurationUnit id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getUnitValue() {
        return this.unitValue;
    }

    public void setUnitValue(Integer unitValue) {
        this.unitValue = unitValue;
    }

    public DurationUnit unitValue(Integer unitValue) {
        this.unitValue = unitValue;
        return this;
    }

    public String getAlies() {
        return this.alies;
    }

    public void setAlies(String alies) {
        this.alies = alies;
    }

    public DurationUnit alies(String alies) {
        this.alies = alies;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DurationUnit name(String name) {
        this.name = name;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DurationUnit)) {
            return false;
        }
        return id != null && id.equals(((DurationUnit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DurationUnit{" +
            "id=" + getId() +
            ", unitValue=" + getUnitValue() +
            ", alies='" + getAlies() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
