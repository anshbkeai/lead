package com.leaderboard.lead.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeSet;

import org.springframework.stereotype.Service;

import com.leaderboard.lead.DTO.ScoreRequestDTO;
import com.leaderboard.lead.DTO.User;

@Service
public class LeaderBoardService {

    private HashMap<String,Integer> map;
    private PriorityQueue<User> priorityQueue;
    private TreeSet<User> treeSet;
    private Integer K;

    public LeaderBoardService() {
        this.map = new HashMap<>();
        this.priorityQueue = new PriorityQueue<>((a,b) -> Integer.compare(a.getScore(), b.getScore()));
       this.treeSet = new TreeSet<>((a,b) -> {
            int scoreCompare = (a.getUserId().compareTo(b.getUserId())); 
            return (scoreCompare ==0 ) ? scoreCompare : Integer.compare(b.getScore() , a.getScore());
             
       });
        this.K = 10; // defualt
    }

    public void setK(int K) {
        this.K = K;
    }

    public String addData(ScoreRequestDTO requestDTO) {
        map.put(requestDTO.getUserId(), map.getOrDefault(requestDTO.getUserId(), 0) + requestDTO.getScore());
        
        User user = User.builder()
                        .score(map.get(requestDTO.getUserId()))
                        .userId(requestDTO.getUserId()).build();

        
            treeSet.remove(user);
         
        if(treeSet.size() < K ) {
            treeSet.add(user);
        }
        else {
            if(treeSet.getLast().getScore() < user.getScore()) {
                treeSet.pollLast();
                treeSet.add(user);
            } 
        }

        return requestDTO.toString();
    }

    public List<User> getTopK() {
        return new ArrayList<>(treeSet);
    } 
}
