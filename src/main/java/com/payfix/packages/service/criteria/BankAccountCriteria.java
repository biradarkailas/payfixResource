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
 * Criteria class for the {@link com.payfix.packages.domain.BankAccount} entity. This class is used
 * in {@link com.payfix.packages.web.rest.BankAccountResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bank-accounts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BankAccountCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter accountNumber;

    private StringFilter name;

    private StringFilter ifscCode;

    private StringFilter bankName;

    public BankAccountCriteria() {}

    public BankAccountCriteria(BankAccountCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.accountNumber = other.accountNumber == null ? null : other.accountNumber.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.ifscCode = other.ifscCode == null ? null : other.ifscCode.copy();
        this.bankName = other.bankName == null ? null : other.bankName.copy();
    }

    @Override
    public BankAccountCriteria copy() {
        return new BankAccountCriteria(this);
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

    public StringFilter getAccountNumber() {
        return accountNumber;
    }

    public StringFilter accountNumber() {
        if (accountNumber == null) {
            accountNumber = new StringFilter();
        }
        return accountNumber;
    }

    public void setAccountNumber(StringFilter accountNumber) {
        this.accountNumber = accountNumber;
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

    public StringFilter getIfscCode() {
        return ifscCode;
    }

    public StringFilter ifscCode() {
        if (ifscCode == null) {
            ifscCode = new StringFilter();
        }
        return ifscCode;
    }

    public void setIfscCode(StringFilter ifscCode) {
        this.ifscCode = ifscCode;
    }

    public StringFilter getBankName() {
        return bankName;
    }

    public StringFilter bankName() {
        if (bankName == null) {
            bankName = new StringFilter();
        }
        return bankName;
    }

    public void setBankName(StringFilter bankName) {
        this.bankName = bankName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BankAccountCriteria that = (BankAccountCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(accountNumber, that.accountNumber) &&
            Objects.equals(name, that.name) &&
            Objects.equals(ifscCode, that.ifscCode) &&
            Objects.equals(bankName, that.bankName)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountNumber, name, ifscCode, bankName);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BankAccountCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (accountNumber != null ? "accountNumber=" + accountNumber + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (ifscCode != null ? "ifscCode=" + ifscCode + ", " : "") +
            (bankName != null ? "bankName=" + bankName + ", " : "") +
            "}";
    }
}
