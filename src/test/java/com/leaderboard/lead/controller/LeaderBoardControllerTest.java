package com.leaderboard.lead.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.leaderboard.lead.DTO.ScoreRequestDTO;
import com.leaderboard.lead.DTO.User;
import com.leaderboard.lead.Service.LeaderBoardService;

@ExtendWith(MockitoExtension.class)
public class LeaderBoardControllerTest {

    // moc 
    @InjectMocks
    private LeaderBoardService leaderBoardService;

    private User user;
    private ScoreRequestDTO scoreRequestDTO;
    @BeforeEach
    public void setup() {
            user = User.builder().score(100).userId("user-123").build();
            scoreRequestDTO =  ScoreRequestDTO.builder().score(50).userId("user-123").build();
    }

    @Test
    void testAdd() {
        
    }
}
