package com.payfix.packages.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Logo.
 */
@Entity
@Table(name = "logo")
public class Logo extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "term_conditions")
    private String termConditions;

    @Column(name = "about")
    private String about;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Duration duration;

    @ManyToOne
    private DurationUnit durationUnit;

    @ManyToOne
    private PricePerDay pricePerDay;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Logo id(Long id) {
        this.id = id;
        return this;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public Logo imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTermConditions() {
        return this.termConditions;
    }

    public Logo termConditions(String termConditions) {
        this.termConditions = termConditions;
        return this;
    }

    public void setTermConditions(String termConditions) {
        this.termConditions = termConditions;
    }

    public String getAbout() {
        return this.about;
    }

    public Logo about(String about) {
        this.about = about;
        return this;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public User getUser() {
        return this.user;
    }

    public Logo user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return this.category;
    }

    public Logo category(Category category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Duration getDuration() {
        return this.duration;
    }

    public Logo duration(Duration duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public DurationUnit getDurationUnit() {
        return this.durationUnit;
    }

    public Logo durationUnit(DurationUnit durationUnit) {
        this.setDurationUnit(durationUnit);
        return this;
    }

    public void setDurationUnit(DurationUnit durationUnit) {
        this.durationUnit = durationUnit;
    }

    public PricePerDay getPricePerDay() {
        return this.pricePerDay;
    }

    public Logo pricePerDay(PricePerDay pricePerDay) {
        this.setPricePerDay(pricePerDay);
        return this;
    }

    public void setPricePerDay(PricePerDay pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Logo)) {
            return false;
        }
        return id != null && id.equals(((Logo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Logo{" +
            "id=" + getId() +
            ", imageUrl='" + getImageUrl() + "'" +
            ", termConditions='" + getTermConditions() + "'" +
            ", about='" + getAbout() + "'" +
            "}";
    }
}
