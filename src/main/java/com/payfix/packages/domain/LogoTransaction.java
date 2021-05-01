package com.payfix.packages.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A LogoTransaction.
 */
@Entity
@Table(name = "logo_transaction")
public class LogoTransaction extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Double amount;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "category", "duration", "durationUnit", "pricePerDay" }, allowSetters = true)
    private Logo logo;

    @ManyToOne
    private TransactionType transactionType;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LogoTransaction id(Long id) {
        this.id = id;
        return this;
    }

    public Double getAmount() {
        return this.amount;
    }

    public LogoTransaction amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public User getUser() {
        return this.user;
    }

    public LogoTransaction user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Logo getLogo() {
        return this.logo;
    }

    public LogoTransaction logo(Logo logo) {
        this.setLogo(logo);
        return this;
    }

    public void setLogo(Logo logo) {
        this.logo = logo;
    }

    public TransactionType getTransactionType() {
        return this.transactionType;
    }

    public LogoTransaction transactionType(TransactionType transactionType) {
        this.setTransactionType(transactionType);
        return this;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LogoTransaction)) {
            return false;
        }
        return id != null && id.equals(((LogoTransaction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LogoTransaction{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            "}";
    }
}
