package com.leaderboard.lead.controller;

import static org.mockito.Mockito.when;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.leaderboard.lead.Config.BuckerPerUserHashMap;
import com.leaderboard.lead.Config.BucketPerUserCache;
import com.leaderboard.lead.Controllers.LeaderBoardContoller;
import com.leaderboard.lead.DTO.ScoreRequestDTO;
import com.leaderboard.lead.DTO.User;
import com.leaderboard.lead.Service.LeaderBoardService;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

@WebMvcTest(LeaderBoardContoller.class)
public class LeaderBoardControllerTest {

    // moc 
    @Autowired
   private MockMvc mockMvc;

   @MockitoBean
   private LeaderBoardService leaderBoardService;

   @MockitoBean
   private BuckerPerUserHashMap buckerPerUserHashMap;

   @MockitoBean
   private BucketPerUserCache bucketPerUserCache;
   private Bucket bucket;

    private User user;
    private ScoreRequestDTO scoreRequestDTO;
    @BeforeEach
    public void setup() {
            user = User.builder().score(100).userId("user-123").build();
            scoreRequestDTO =  ScoreRequestDTO.builder().score(50).userId("user-123").build();
            bucket = Bucket.builder().addLimit(Bandwidth.classic(10, Refill.intervally(1, Duration.ofSeconds(1)))).build();
    }

    @Test
    void testAdd() throws Exception {
        String responseAdd = scoreRequestDTO.toString();
        when(leaderBoardService.addData(scoreRequestDTO)).thenReturn(scoreRequestDTO.toString());
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/v1/score")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content("{\"userId\":\"user-123\",\"score\":50}"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content().string(responseAdd));
    }

    @Test
    void testAddWhenBucketFull() throws Exception {
        when(buckerPerUserHashMap.isAllowed(scoreRequestDTO.getUserId())).thenReturn(false);
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/v2/score")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":\"user-123\",\"score\":50}"))
                        .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isTooManyRequests())
                        .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content().string("to MANY REQUEST"));
                        
        

    }
    @Test
    void testAddWhenBucketCache() throws Exception {
        when(bucketPerUserCache.isAllowed(scoreRequestDTO.getUserId())).thenReturn(false);
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/v3/score")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":\"user-123\",\"score\":50}"))
                        .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isTooManyRequests())
                        .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content().string("to MANY REQUEST"));
                        
        

    }
}
