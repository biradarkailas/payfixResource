package com.payfix.packages.service.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.payfix.packages.domain.UserMemberShip} entity.
 */
public class UserMemberShipDTO implements Serializable {

    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private Long userId;

    private String userName;

    private Long memberShipId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getMemberShipId() {
        return memberShipId;
    }

    public void setMemberShipId(Long memberShipId) {
        this.memberShipId = memberShipId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserMemberShipDTO)) {
            return false;
        }

        UserMemberShipDTO userMemberShipDTO = (UserMemberShipDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userMemberShipDTO.id);
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
            .append("startDate", startDate)
            .append("endDate", endDate)
            .append("userId", userId)
            .append("userName", userName)
            .append("memberShipId", memberShipId)
            .toString();
    }
}
