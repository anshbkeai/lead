package com.leaderboard.lead.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScoreRequestDTO {

    private String userId;
    private Integer score;
}
