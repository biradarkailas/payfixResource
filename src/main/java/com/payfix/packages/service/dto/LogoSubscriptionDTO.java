package com.payfix.packages.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.payfix.packages.domain.LogoSubscription} entity.
 */
public class LogoSubscriptionDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate date;

    private UserDTO user;

    private LogoDTO logo;

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
            ", user=" + getUser() +
            ", logo=" + getLogo() +
            "}";
    }
}
