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

    private HashMap<String,User> map;
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

   public String addData(ScoreRequestDTO scoreRequestDTO ) {
        // the poins i if user is in them amap then 
        if(map.containsKey(scoreRequestDTO.getUserId())) {
            User existingUser = map.get(scoreRequestDTO.getUserId());
            priorityQueue.remove(existingUser);

            existingUser.setScore(existingUser.getScore() + scoreRequestDTO.getScore());
            priorityQueue.add(existingUser);
            map.put(existingUser.getUserId(), existingUser);
        }   
        else {
            User user = User.builder().score(scoreRequestDTO.getScore()).userId(scoreRequestDTO.getUserId()).build();
            map.put(user.getUserId() , user);
            priorityQueue.add(user); 
        }

        if(priorityQueue.size() > K ) {
            priorityQueue.poll();
        }

        return map.get(scoreRequestDTO.getUserId()).toString();
        
   }
    public List<User> getTopK() {
        return new ArrayList<>(priorityQueue); // it does not means that it is soresre
    } 
}
