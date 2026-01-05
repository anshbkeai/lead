package com.leaderboard.lead.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leaderboard.lead.DTO.ScoreRequestDTO;
import com.leaderboard.lead.DTO.User;
import com.leaderboard.lead.Service.LeaderBoardService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LeaderBoardContoller {

    private final LeaderBoardService leaderBoardService;
    @PostMapping("/score")
    public String addScore(@RequestBody ScoreRequestDTO entity) {
        //TODO: process POST request
        
        return leaderBoardService.addData(entity);
    }

    @GetMapping("/leaderBoard")
    public List<User> getLeaderBoard() {
        return  leaderBoardService.getTopK();
    }
    
    
}
