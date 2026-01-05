package com.leaderboard.lead.DTO;

import java.util.Objects;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

    private String userId;
    private Integer score;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

}
