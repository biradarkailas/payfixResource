package com.payfix.packages.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.payfix.packages.domain.CompanyDetails} entity.
 */
public class CompanyDetailsDTO implements Serializable {

    private Long id;

    private String name;

    private String address;

    private String city;

    private Integer registrationNumber;

    private LocalDate registrationDate;

    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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
        if (!(o instanceof CompanyDetailsDTO)) {
            return false;
        }

        CompanyDetailsDTO companyDetailsDTO = (CompanyDetailsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, companyDetailsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyDetailsDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", city='" + getCity() + "'" +
            ", registrationNumber=" + getRegistrationNumber() +
            ", registrationDate='" + getRegistrationDate() + "'" +
            ", userId=" + getUserId() +
            "}";
    }
}
