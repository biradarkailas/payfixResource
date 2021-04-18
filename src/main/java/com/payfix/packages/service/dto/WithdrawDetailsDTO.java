package com.payfix.packages.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.payfix.packages.domain.WithdrawDetails} entity.
 */
public class WithdrawDetailsDTO implements Serializable {

    private Long id;

    private Double amount;

    private ZonedDateTime date;

    private UserDTO user;

    private WithdrawStatusDTO withdrawStatus;

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

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public WithdrawStatusDTO getWithdrawStatus() {
        return withdrawStatus;
    }

    public void setWithdrawStatus(WithdrawStatusDTO withdrawStatus) {
        this.withdrawStatus = withdrawStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WithdrawDetailsDTO)) {
            return false;
        }

        WithdrawDetailsDTO withdrawDetailsDTO = (WithdrawDetailsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, withdrawDetailsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WithdrawDetailsDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", date='" + getDate() + "'" +
            ", user=" + getUser() +
            ", withdrawStatus=" + getWithdrawStatus() +
            "}";
    }
}
