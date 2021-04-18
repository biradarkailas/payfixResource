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
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.payfix.packages.domain.WithdrawDetails} entity. This class is used
 * in {@link com.payfix.packages.web.rest.WithdrawDetailsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /withdraw-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class WithdrawDetailsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter amount;

    private ZonedDateTimeFilter date;

    private LongFilter userId;

    private LongFilter withdrawStatusId;

    public WithdrawDetailsCriteria() {}

    public WithdrawDetailsCriteria(WithdrawDetailsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.withdrawStatusId = other.withdrawStatusId == null ? null : other.withdrawStatusId.copy();
    }

    @Override
    public WithdrawDetailsCriteria copy() {
        return new WithdrawDetailsCriteria(this);
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

    public DoubleFilter getAmount() {
        return amount;
    }

    public DoubleFilter amount() {
        if (amount == null) {
            amount = new DoubleFilter();
        }
        return amount;
    }

    public void setAmount(DoubleFilter amount) {
        this.amount = amount;
    }

    public ZonedDateTimeFilter getDate() {
        return date;
    }

    public ZonedDateTimeFilter date() {
        if (date == null) {
            date = new ZonedDateTimeFilter();
        }
        return date;
    }

    public void setDate(ZonedDateTimeFilter date) {
        this.date = date;
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

    public LongFilter getWithdrawStatusId() {
        return withdrawStatusId;
    }

    public LongFilter withdrawStatusId() {
        if (withdrawStatusId == null) {
            withdrawStatusId = new LongFilter();
        }
        return withdrawStatusId;
    }

    public void setWithdrawStatusId(LongFilter withdrawStatusId) {
        this.withdrawStatusId = withdrawStatusId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final WithdrawDetailsCriteria that = (WithdrawDetailsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(date, that.date) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(withdrawStatusId, that.withdrawStatusId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, date, userId, withdrawStatusId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WithdrawDetailsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (amount != null ? "amount=" + amount + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (withdrawStatusId != null ? "withdrawStatusId=" + withdrawStatusId + ", " : "") +
            "}";
    }
}
