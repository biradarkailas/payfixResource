package com.payfix.packages.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A WithdrawDetails.
 */
@Entity
@Table(name = "withdraw_details")
public class WithdrawDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "date")
    private ZonedDateTime date;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @ManyToOne(optional = false)
    @NotNull
    private WithdrawStatus withdrawStatus;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WithdrawDetails id(Long id) {
        this.id = id;
        return this;
    }

    public Double getAmount() {
        return this.amount;
    }

    public WithdrawDetails amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public WithdrawDetails date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public User getUser() {
        return this.user;
    }

    public WithdrawDetails user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public WithdrawStatus getWithdrawStatus() {
        return this.withdrawStatus;
    }

    public WithdrawDetails withdrawStatus(WithdrawStatus withdrawStatus) {
        this.setWithdrawStatus(withdrawStatus);
        return this;
    }

    public void setWithdrawStatus(WithdrawStatus withdrawStatus) {
        this.withdrawStatus = withdrawStatus;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WithdrawDetails)) {
            return false;
        }
        return id != null && id.equals(((WithdrawDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WithdrawDetails{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
