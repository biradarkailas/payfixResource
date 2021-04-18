package com.payfix.packages.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A UserDetails.
 */
@Entity
@Table(name = "user_details")
public class UserDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "registration_number")
    private Integer registrationNumber;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "mobile_number")
    private Integer mobileNumber;

    @OneToOne(optional = false)
    @NotNull
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDetails id(Long id) {
        this.id = id;
        return this;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public UserDetails companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getRegistrationNumber() {
        return this.registrationNumber;
    }

    public UserDetails registrationNumber(Integer registrationNumber) {
        this.registrationNumber = registrationNumber;
        return this;
    }

    public void setRegistrationNumber(Integer registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public LocalDate getRegistrationDate() {
        return this.registrationDate;
    }

    public UserDetails registrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
        return this;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Double getBalance() {
        return this.balance;
    }

    public UserDetails balance(Double balance) {
        this.balance = balance;
        return this;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Integer getMobileNumber() {
        return this.mobileNumber;
    }

    public UserDetails mobileNumber(Integer mobileNumber) {
        this.mobileNumber = mobileNumber;
        return this;
    }

    public void setMobileNumber(Integer mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public User getUser() {
        return this.user;
    }

    public UserDetails user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserDetails)) {
            return false;
        }
        return id != null && id.equals(((UserDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserDetails{" +
            "id=" + getId() +
            ", companyName='" + getCompanyName() + "'" +
            ", registrationNumber=" + getRegistrationNumber() +
            ", registrationDate='" + getRegistrationDate() + "'" +
            ", balance=" + getBalance() +
            ", mobileNumber=" + getMobileNumber() +
            "}";
    }
}
