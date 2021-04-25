package com.payfix.packages.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.payfix.packages.domain.UserDetails} entity.
 */
public class UserDetailsDTO implements Serializable {

    private Long id;

    private String companyName;

    private Integer registrationNumber;

    private LocalDate registrationDate;

    private Double balance;

    private Integer mobileNumber;

    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(Integer registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Integer getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(Integer mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserDetailsDTO)) {
            return false;
        }

        UserDetailsDTO userDetailsDTO = (UserDetailsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userDetailsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserDetailsDTO{" +
            "id=" + getId() +
            ", companyName='" + getCompanyName() + "'" +
            ", registrationNumber=" + getRegistrationNumber() +
            ", registrationDate='" + getRegistrationDate() + "'" +
            ", balance=" + getBalance() +
            ", mobileNumber=" + getMobileNumber() +
            ", userId=" + getUserId() +
            "}";
    }
}
