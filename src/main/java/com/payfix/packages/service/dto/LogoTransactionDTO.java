package com.payfix.packages.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.payfix.packages.domain.LogoTransaction} entity.
 */
public class LogoTransactionDTO implements Serializable {

    private Long id;

    @NotNull
    private Double amount;

    private UserDTO user;

    private LogoDTO logo;

    private TransactionTypeDTO transactionType;

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

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public LogoDTO getLogo() {
        return logo;
    }

    public void setLogo(LogoDTO logo) {
        this.logo = logo;
    }

    public TransactionTypeDTO getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionTypeDTO transactionType) {
        this.transactionType = transactionType;
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
        return "LogoTransactionDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", user=" + getUser() +
            ", logo=" + getLogo() +
            ", transactionType=" + getTransactionType() +
            "}";
    }
}
