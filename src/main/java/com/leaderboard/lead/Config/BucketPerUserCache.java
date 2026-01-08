package com.leaderboard.lead.Config;

import java.time.Duration;

import org.springframework.stereotype.Component;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BucketPerUserCache {

    private Cache<String,Bucket> cache = Caffeine.newBuilder()
                                            .expireAfterAccess(Duration.ofMinutes(25))
                                            .maximumSize(1000)
                                            .build();
    
    public boolean isAllowed(String userId) {
        Bucket bucket =  cache.get(userId,this::newBucket );
        cache.put(userId, bucket);
        return bucket.tryConsume(1);
    }

    public Bucket newBucket(String userid) {
        log.info("Creating  the Bucket for the User id using the CaCHE {}" , userid);
        Bucket bucket = Bucket.builder()
                            .addLimit(Bandwidth.classic(2, Refill.intervally(2, Duration.ofMinutes(2))))
                            .build();
        return bucket;
    }
}
