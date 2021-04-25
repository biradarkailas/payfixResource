package com.payfix.packages.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.payfix.packages.domain.LogoSubscription} entity.
 */
public class LogoSubscriptionDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate date;

    private Long userId;

    private Long logoId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LogoSubscriptionDTO)) {
            return false;
        }

        LogoSubscriptionDTO logoSubscriptionDTO = (LogoSubscriptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, logoSubscriptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LogoSubscriptionDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", userId=" + getUserId() +
            ", logoId=" + getLogoId() +
            "}";
    }
}
