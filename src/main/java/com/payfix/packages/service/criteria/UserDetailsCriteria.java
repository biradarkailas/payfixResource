package com.payfix.packages.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.payfix.packages.domain.UserDetails} entity. This class is used
 * in {@link com.payfix.packages.web.rest.UserDetailsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UserDetailsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter companyName;

    private IntegerFilter registrationNumber;

    private LocalDateFilter registrationDate;

    private DoubleFilter balance;

    private IntegerFilter mobileNumber;

    private LongFilter userId;

    public UserDetailsCriteria() {}

    public UserDetailsCriteria(UserDetailsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.companyName = other.companyName == null ? null : other.companyName.copy();
        this.registrationNumber = other.registrationNumber == null ? null : other.registrationNumber.copy();
        this.registrationDate = other.registrationDate == null ? null : other.registrationDate.copy();
        this.balance = other.balance == null ? null : other.balance.copy();
        this.mobileNumber = other.mobileNumber == null ? null : other.mobileNumber.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public UserDetailsCriteria copy() {
        return new UserDetailsCriteria(this);
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

    public StringFilter getCompanyName() {
        return companyName;
    }

    public StringFilter companyName() {
        if (companyName == null) {
            companyName = new StringFilter();
        }
        return companyName;
    }

    public void setCompanyName(StringFilter companyName) {
        this.companyName = companyName;
    }

    public IntegerFilter getRegistrationNumber() {
        return registrationNumber;
    }

    public IntegerFilter registrationNumber() {
        if (registrationNumber == null) {
            registrationNumber = new IntegerFilter();
        }
        return registrationNumber;
    }

    public void setRegistrationNumber(IntegerFilter registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public LocalDateFilter getRegistrationDate() {
        return registrationDate;
    }

    public LocalDateFilter registrationDate() {
        if (registrationDate == null) {
            registrationDate = new LocalDateFilter();
        }
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateFilter registrationDate) {
        this.registrationDate = registrationDate;
    }

    public DoubleFilter getBalance() {
        return balance;
    }

    public DoubleFilter balance() {
        if (balance == null) {
            balance = new DoubleFilter();
        }
        return balance;
    }

    public void setBalance(DoubleFilter balance) {
        this.balance = balance;
    }

    public IntegerFilter getMobileNumber() {
        return mobileNumber;
    }

    public IntegerFilter mobileNumber() {
        if (mobileNumber == null) {
            mobileNumber = new IntegerFilter();
        }
        return mobileNumber;
    }

    public void setMobileNumber(IntegerFilter mobileNumber) {
        this.mobileNumber = mobileNumber;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserDetailsCriteria that = (UserDetailsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(companyName, that.companyName) &&
            Objects.equals(registrationNumber, that.registrationNumber) &&
            Objects.equals(registrationDate, that.registrationDate) &&
            Objects.equals(balance, that.balance) &&
            Objects.equals(mobileNumber, that.mobileNumber) &&
            Objects.equals(userId, that.userId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, companyName, registrationNumber, registrationDate, balance, mobileNumber, userId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserDetailsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (companyName != null ? "companyName=" + companyName + ", " : "") +
            (registrationNumber != null ? "registrationNumber=" + registrationNumber + ", " : "") +
            (registrationDate != null ? "registrationDate=" + registrationDate + ", " : "") +
            (balance != null ? "balance=" + balance + ", " : "") +
            (mobileNumber != null ? "mobileNumber=" + mobileNumber + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }
}
