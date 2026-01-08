package com.leaderboard.lead.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leaderboard.lead.Config.BuckerPerUserHashMap;
import com.leaderboard.lead.Config.BucketPerUserCache;
import com.leaderboard.lead.DTO.ScoreRequestDTO;
import com.leaderboard.lead.DTO.User;
import com.leaderboard.lead.Service.LeaderBoardService;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Duration;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LeaderBoardContoller {

    private final LeaderBoardService leaderBoardService;
    private final BuckerPerUserHashMap buckerPerUserHashMap;
    private final BucketPerUserCache bucketPerUserCache;

    private Bucket bucket;
    
    @PostConstruct
    public void init() {
        Bandwidth bandwidth = Bandwidth.classic(2, Refill.intervally(2, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder()
                        .addLimit(bandwidth)
                        .build();
    }

    @PostMapping("/v1/score")
    public ResponseEntity<String> addScore(@RequestBody ScoreRequestDTO entity) {
        //TODO: process POST request
        
        if(bucket.tryConsume(1)) {
            return ResponseEntity.ok(leaderBoardService.addData(entity));
        }
         return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("to MANY REQUEST");
    }
    @PostMapping("/score")
    public ResponseEntity<String> addScoreInceptor(@RequestBody ScoreRequestDTO entity) {
        //TODO: process POST request

            return ResponseEntity.ok(leaderBoardService.addData(entity));
    }

    @PostMapping("/v2/score")
    public ResponseEntity<String> addScoreV2(@RequestBody ScoreRequestDTO entity) {
        //TODO: process POST request
        
        if(buckerPerUserHashMap.isAllowed(entity.getUserId())) {
            return ResponseEntity.ok(leaderBoardService.addData(entity));
        }
         return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("to MANY REQUEST");
    }
    @PostMapping("/v3/score")
    public ResponseEntity<String> addScoreV3(@RequestBody ScoreRequestDTO entity) {
        //TODO: process POST request
        
        if(bucketPerUserCache.isAllowed(entity.getUserId())) {
            return ResponseEntity.ok(leaderBoardService.addData(entity));
        }
         return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("to MANY REQUEST");
    }

    @GetMapping("/leaderBoard")
    public List<User> getLeaderBoard() {
        return  leaderBoardService.getTopK();
    }
    
    
}
