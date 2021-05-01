package com.payfix.packages.service.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.payfix.packages.domain.LogoTransaction} entity.
 */
public class LogoTransactionDTO implements Serializable {

    private Long id;

    @NotNull
    private Double amount;

    private Long userId;

    private Long logoId;

    private Long transactionTypeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getLogoId() {
        return logoId;
    }

    public void setLogoId(Long logoId) {
        this.logoId = logoId;
    }

    public Long getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(Long transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LogoTransactionDTO)) {
            return false;
        }

        LogoTransactionDTO logoTransactionDTO = (LogoTransactionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, logoTransactionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("id", id)
            .append("amount", amount)
            .append("userId", userId)
            .append("logoId", logoId)
            .append("transactionTypeId", transactionTypeId)
            .toString();
    }
}
